from django.conf.urls import url

from . import views

urlpatterns = [
  url(r'^$', views.upload),
  url(r'upload$', views.upload, name='upload'),
]

