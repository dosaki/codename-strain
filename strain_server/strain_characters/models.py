from django.db import models
from user.models import User

# Create your models here.
class Faction(models.Model):
    name = models.CharField(max_length=32)
    description = models.CharField(max_length=256)
    best_score = models.IntegerField(default=0)
    total_score = models.IntegerField(default=0)

    def __str__(self):
        return self.name

class CharacterClass(models.Model):
    name = models.CharField(max_length=32)
    health = models.IntegerField(default=1)
    strength = models.IntegerField(default=1)
    accuracy = models.IntegerField(default=1)
    stamina = models.IntegerField(default=1)
    perception = models.IntegerField(default=1)
    weaponTypes = models.CharField(max_length=256)
    armourTypes = models.CharField(max_length=256)

    def __str__(self):
        return self.name

class Character(models.Model):
    #General Info
    user = models.ForeignKey(User)
    faction = models.ForeignKey(Faction)
    charClass = models.ForeignKey(CharacterClass)
    position = models.CharField(max_length=256)
    active = models.BooleanField(default=False)
    #Stats and such
    maxHealth = models.IntegerField(default=1)
    health = models.IntegerField(default=1)
    strength = models.IntegerField(default=1)
    accuracy = models.IntegerField(default=1)
    stamina = models.IntegerField(default=1)
    perception = models.IntegerField(default=1)
    #Possessions
    money = models.IntegerField(default=200)

    def adoptClass(self, characterClass):
        self.charClass = characterClass
        #Stats and such
        self.maxHealth = characterClass.health
        self.health = characterClass.health
        self.strength = characterClass.strength
        self.accuracy = characterClass.accuracy
        self.stamina = characterClass.stamina
        self.perception = characterClass.perception
        #todo: add up bonuses

    def __str__(self):
        return self.user.username + "(" + str(self.faction) + " " + self.charClass.name + ")"
