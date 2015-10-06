from django.shortcuts import render
from django.http import HttpResponse
from django.template import RequestContext, loader

def temp_page(req):
  context = RequestContext(req, {'temperature': get_temp()})
  return render(req, 'tempserver/temperature.html', context)

def get_temp_req(req):
  return HttpResponse(get_temp())

def get_temp():
  # TODO(dlila): implement this properly
  return "22.1"

# Create your views here.
