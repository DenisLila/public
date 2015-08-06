# It probably would have been better to implement this as macros using genrules.
# All we're doing is creating folders and copy files. Oh, well. Too late.

def ino_lib_impl(ctx):
  libfolder = ctx.outputs.libfolder.path

  cmd = ("set -e;" +
    "rm -rf %s ;" % (libfolder) +
    "mkdir %s;" % (libfolder))

  for src in ctx.files.srcs:
    cmd += "cp %s %s;" % (src.path, libfolder)

  ctx.action(
    inputs = ctx.files.srcs,
    outputs = [ctx.outputs.libfolder],
    command = cmd,
    use_default_shell_env = True)
  return struct(lib = ctx.outputs.libfolder)

ino_lib = rule(
  implementation = ino_lib_impl,
  attrs = {
    "srcs": attr.label_list(allow_files = True, mandatory = True, non_empty = True),
  },
  outputs = {
    "libfolder": "%{name}",
  },
  output_to_genfiles = True,
)

def ino_bin_impl(ctx):
  # create the ino project folder with empty lib and src directories.
  inofolder = ctx.outputs.inofolder.path
  print(ctx.outputs.inofolder.path)
  cmd = "set -e; rm -rf %s; mkdir %s;" % (inofolder, inofolder)
  cmd += "mkdir %s/lib; mkdir %s/src;" % (inofolder, inofolder)
  # Copy the .ino file to the src directory.
  ino = ctx.file.src.path
  cmd += "cp %s %s/src;" % (ino, inofolder)
  # Copy the libraries into the lib folder.
  libinputs = []
  for inolib in ctx.attr.inolibs:
    libinputs += [inolib.lib]
    cmd += "cp -r %s %s/lib;" % (inolib.lib.path, inofolder)

  ctx.action(
    inputs = libinputs + [ctx.file.src],
    outputs = [ctx.outputs.inofolder],
    command = cmd,
    use_default_shell_env = True)

  build_cmd = '\n'.join([
    '#!/bin/bash',
    'pushd .',
    'cd %s' % inofolder,
    'ino clean',
    'ino build',
    'if [[ "$1" = "-u" ]]; then',
    '  CMD="ino upload "',
    '  if [[ ! -z "$2" ]]; then',
    '    CMD="$CMD -p $2"',
    '  fi',
    '  $CMD',
    'fi',
    'popd'])

  ctx.file_action(
    output = ctx.outputs.executable,
    content = build_cmd,
    executable = True)

# This will create a folder {name} with two subdirectories:
# src and lib. Src will contain the single .ino file in the src attribute.
# lib will contain the library folders produced by the inolibs rules.
ino_bin = rule(
  implementation = ino_bin_impl,
  attrs = {
    # A single .ino file.
    "src": attr.label(allow_files = True, mandatory = True, single_file = True),
    # ino_lib rules.
    "inolibs": attr.label_list(allow_files = False, mandatory = False),
  },
  outputs = {
    "inofolder": "%{name}-ino-proj"
  },
  executable = True,
)

