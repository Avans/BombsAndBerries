from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'nameserver.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^callback', 'views.avans_callback'),
    url(r'^(?P<inlognaam>[a-z]+)', 'views.get_name'),
    url(r'^', 'views.avans_login')

)
