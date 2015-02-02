from django.shortcuts import render
from django.http import HttpResponse
from django.core import serializers
import json

from strain_characters.models import Character
from strain_characters.models import CharacterClass
from strain_characters.models import Faction
from user.models import User

# Create your views here.
def index(request):
    return HttpResponse("Hello, world. You're at the polls index.")

def username(request, name):
    user = User.objects.get(username=name)
    return HttpResponse("> Sup, " + str(user.username) + "! (" + str(user.email) + ")")

def classData(request, className):
    charClass = CharacterClass.objects.get(name=className.capitalize())
    return HttpResponse(serializers.serialize('json', [charClass]), content_type="application/json")

def register(request):
    params = request.GET
    response = {}
    if not User.objects.filter(username=params['name']):
        user = User(username=params['name'], email=params['email'])
        user.save()
        #Create Infected Character
        infectedClass = CharacterClass.objects.get(name="Drone")
        infectedCharacter = Character(
            user = user,
            faction = Faction.objects.get(name="Infected"),
            charClass = infectedClass,
            position = "0:0"
        )
        infectedCharacter.adoptClass(infectedClass)
        infectedCharacter.save()
        #Create Human Character
        humanClass = CharacterClass.objects.get(name="Soldier")
        humanCharacter = Character(
            user = user,
            faction = Faction.objects.get(name="Humans"),
            charClass = humanClass,
            position = "0:0"
        )
        humanCharacter.adoptClass(humanClass)
        humanCharacter.save()

        response['result'] = "success"
        response['message'] = "User created."
        return  HttpResponse(serializers.serialize('json', [user]), content_type="application/json")
    else:
        response['result'] = "failure"
        response['message'] = "User already exists."
        return  HttpResponse(json.dumps(response), content_type="application/json")

def setup(request):
    #Factions
    infectedFaction = Faction(name="Infected", description="Infected faction")
    infectedFaction.save()
    humanFaction = Faction(name="Humans", description="Human faction")
    humanFaction.save()
    #Classes
    soldier = CharacterClass(
        name = "Soldier",
        health = 120,
        strength = 4,
        accuracy = 6,
        stamina = 5,
        perception = 5,
        weaponTypes = "",
        armourTypes = ""
    )
    soldier.save()
    scientist = CharacterClass(
        name = "Scientist",
        health = 100,
        strength = 4,
        accuracy = 4,
        stamina = 5,
        perception = 7,
        weaponTypes = "",
        armourTypes = ""
    )
    scientist.save()
    drone = CharacterClass(
        name = "Drone",
        health = 120,
        strength = 7,
        accuracy = 3,
        stamina = 7,
        perception = 3,
        weaponTypes = "",
        armourTypes = ""
    )
    drone.save()
    spitter = CharacterClass(
        name = "Spitter",
        health = 120,
        strength = 3,
        accuracy = 7,
        stamina = 5,
        perception = 5,
        weaponTypes = "",
        armourTypes = ""
    )
    spitter.save()
    return  HttpResponse("Done.")
