from django import url

from . import views

urlpatterns = [
  url(r'^$', views.upload_page),
  url(r'upload_page$', views.upload_page),
  url(r'upload$', views.upload),
]

