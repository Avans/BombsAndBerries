from django.shortcuts import render
from django.http import HttpResponse
from django.http import HttpResponseRedirect
from django.http import HttpResponseNotFound
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.contrib import messages
import oauth2 as oauth, cgi, json, base64, urlparse, json
import secrets

AVANS_KEY = secrets.AVANS_KEY
AVANS_SECRET = secrets.AVANS_SECRET
REQUEST_TOKEN_URL = 'https://publicapi.avans.nl/oauth/request_token?oauth_callback=http://%s/callback'
ACCESS_TOKEN_URL = 'https://publicapi.avans.nl/oauth/access_token'
AUTHORIZE_URL = 'https://publicapi.avans.nl/oauth/saml.php?oauth_token=%s'

consumer = oauth.Consumer(AVANS_KEY, AVANS_SECRET)
client = oauth.Client(consumer)

def avans_login(request):
    client = oauth.Client(consumer)
    resp, content = client.request(REQUEST_TOKEN_URL % request.get_host(), "GET")

    if resp['status'] != '200':
        raise Exception("Invalid response from oauth")

    request.session['request_token'] = dict(cgi.parse_qsl(content))

    url = AUTHORIZE_URL % (request.session['request_token']['oauth_token'])
    return HttpResponseRedirect(url)

def avans_callback(request):
    global client
    token = oauth.Token(request.session['request_token']['oauth_token'], request.session['request_token']['oauth_token_secret'])
    token.set_verifier(request.GET['oauth_verifier'])

    client = oauth.Client(consumer, token)

    resp, content = client.request(ACCESS_TOKEN_URL, "GET")
    if resp['status'] != '200':
        raise Exception("Invalid response from Avans.")

    access_token = dict(cgi.parse_qsl(content))
    token = oauth.Token(access_token['oauth_token'], access_token['oauth_token_secret'])
    client = oauth.Client(consumer, token)

    return HttpResponse('OK')


def get_name(request, inlognaam):
    global client

    response, content = client.request('https://publicapi.avans.nl/oauth/people/%s' % inlognaam, 'GET')

    if response['status'] != '200':
        return HttpResponse('Error', status=500)
    else:
        fullname = json.loads(content)['name']['formatted']
        return HttpResponse(fullname)
