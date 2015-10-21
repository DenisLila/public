from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.http import require_GET, require_POST

@require_GET
def upload_page(req):
  return render(req, "fileupload/fileupload.html")

@require_POST
def upload(req):
  pass

