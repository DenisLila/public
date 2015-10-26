from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.http import require_http_methods
from django.core.files.move import file_move_safe
from django.forms import Form
from django.forms import fields

UPLOAD_TO_DIR = "/home/dlila/repos/public/python-server/mysite/fileupload/uploaded_files/"

class UploadForm(Form):
  ff = fields.FileField(label='select a file')

def handle_file(src):
  with open(UPLOAD_TO_DIR + src.name, 'w+') as dest:
    for chunk in src.chunks():
      dest.write(chunk)

@require_http_methods(['GET', 'POST'])
def upload(req):
  if req.method == 'POST':
    form = UploadForm(req.POST, req.FILES)
    if form.is_valid():
      f = req.FILES['ff']
      handle_file(f)
      # TODO(dlila): handle failures in handle_file
      return render(req, 'fileupload/uploadsuccess.html', {'file_name' : f.name})
  else:
    form = UploadForm()
  return render(req, 'fileupload/fileupload.html', {'form' : form})

