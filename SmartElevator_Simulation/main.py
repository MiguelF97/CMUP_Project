import pygame
import time
import paho.mqtt.client as mqtt

pygame.init()

# Ecra
screen = pygame.display.set_mode((800, 600))

# Imagens
bg = pygame.image.load("data/BackGround.png")
elevator = pygame.image.load("data/Elevator.png")
person4 = pygame.image.load("data/People.png")
person3 = pygame.image.load("data/People.png")
person2 = pygame.image.load("data/People.png")
person1 = pygame.image.load("data/People.png")
person0 = pygame.image.load("data/People.png")
personGar = pygame.image.load("data/People.png")

personGarStatus = False
person0Status = False
person1Status = False
person2Status = False
person3Status = False
person4Status = False

personGarWeight = 60
person0Weight = 100
person1Weight = 78
person2Weight = 80
person3Weight = 90
person4Weight = 64

# Titulo e icon
pygame.display.set_caption("Smart Elevator")

# Cor do botao
b_color = (190, 0, 0)
b2_color = (0, 100, 0)

# Where the elevator is
font = pygame.font.Font('freesansbold.ttf', 32)
text = font.render('Floor 0', True, (0, 255, 0), (0, 0, 128))
textRect = text.get_rect()
textRect.center = (530, 30)

# Weight Of the Elevator
text2 = font.render('Weight: ', True, (0, 255, 0), (0, 0, 128))
textRect2 = text2.get_rect()
textRect2.center = (630, 70)

text3 = font.render(time.strftime('%X %x'), True, (0, 255, 0), (0, 0, 128))
textRect3 = text3.get_rect()
textRect3.center = (630, 150)

xElevator = 135
yElevator = 370
whereTheElevatorIs = 4
elevatorWeight = 0


def reload_all():
    screen.blit(bg, (0, 0))
    screen.blit(elevator, (xElevator, yElevator))
    if person4Status:
        screen.blit(person4, (430, 35))
    if person3Status:
        screen.blit(person3, (430, 125))
    if person2Status:
        screen.blit(person2, (430, 215))
    if person1Status:
        screen.blit(person1, (430, 305))
    if person0Status:
        screen.blit(person0, (430, 395))
    if personGarStatus:
        screen.blit(personGar, (430, 515))
    pygame.draw.rect(screen, b_color, [325, 55, 10, 10])
    pygame.draw.rect(screen, b_color, [325, 145, 10, 10])
    pygame.draw.rect(screen, b_color, [325, 235, 10, 10])
    pygame.draw.rect(screen, b_color, [325, 325, 10, 10])
    pygame.draw.rect(screen, b_color, [325, 415, 10, 10])
    pygame.draw.rect(screen, b_color, [325, 535, 10, 10])
    pygame.draw.rect(screen, b2_color, [395, 415, 10, 10])
    pygame.draw.rect(screen, b2_color, [395, 535, 10, 10])
    screen.blit(text, textRect)
    screen.blit(text2, textRect2)
    screen.blit(text3, textRect3)
    pygame.display.update()


last = pygame.time.get_ticks()
cooldown = 300

#MQTT
mqttc = mqtt.Client("clientId-kN5c0OZ02m", clean_session=False)
#mqttc.username_pw_set("Francisco", "SmartElevator2021")
mqttc.connect("broker.mqttdashboard.com", 1883)

# Loop
running = True
while running:
    mouse = pygame.mouse.get_pos()
    text3 = font.render(time.strftime('%X %x'), True, (0, 255, 0), (0, 0, 128))
    for event in pygame.event.get():

        if event.type == pygame.MOUSEBUTTONDOWN:
            # botao floor 4
            if 325 <= mouse[0] <= 325 + 10 and 55 <= mouse[1] <= 55 + 10:
                if whereTheElevatorIs != 4:
                    mqttc.publish("smartElevator/elevatorTrips",
                                  payload=str(whereTheElevatorIs) + ";" + str(4) + ";" + str(whereTheElevatorIs) + ";"
                                  + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                                  qos=0)
                    for index in range(4 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
            # botao floor 3
            if 325 <= mouse[0] <= 325 + 10 and 145 <= mouse[1] <= 145 + 10:
                mqttc.publish("smartElevator/elevatorTrips",
                              payload=str(whereTheElevatorIs) + ";" + str(3) + ";" + str(whereTheElevatorIs) + ";"
                              + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                              qos=0)
                if whereTheElevatorIs > 3:
                    for index in range(whereTheElevatorIs - 3):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()
                if whereTheElevatorIs < 3:
                    for index in range(3 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
            # botao floor 2
            if 325 <= mouse[0] <= 325 + 10 and 235 <= mouse[1] <= 235 + 10:
                mqttc.publish("smartElevator/elevatorTrips",
                              payload=str(whereTheElevatorIs) + ";" + str(2) + ";" + str(whereTheElevatorIs) + ";"
                              + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                              qos=0)
                if whereTheElevatorIs > 2:
                    for index in range(whereTheElevatorIs - 2):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()
                if whereTheElevatorIs < 2:
                    for index in range(2 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
            # botao floor 1
            if 325 <= mouse[0] <= 325 + 10 and 325 <= mouse[1] <= 325 + 10:
                mqttc.publish("smartElevator/elevatorTrips",
                              payload=str(whereTheElevatorIs) + ";" + str(1) + ";" + str(whereTheElevatorIs) + ";"
                              + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                              qos=0)
                if whereTheElevatorIs > 1:
                    for index in range(whereTheElevatorIs - 1):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()
                if whereTheElevatorIs < 1:
                    for index in range(1 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
            # botao floor 0
            if 325 <= mouse[0] <= 325 + 10 and 415 <= mouse[1] <= 415 + 10:
                mqttc.publish("smartElevator/elevatorTrips",
                              payload=str(whereTheElevatorIs) + ";" + str(0) + ";" + str(whereTheElevatorIs) + ";"
                              + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                              qos=0)
                if whereTheElevatorIs > 0:
                    for index in range(whereTheElevatorIs - 0):
                        yElevator += 90
                        pygame.time.wait(100)
                        elevatorWeight = 0
                        reload_all()
                if whereTheElevatorIs < 0:
                    for index in range(0 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
                elevatorWeight = 0
                text2 = font.render('Weight: ' + str(elevatorWeight), True, (0, 255, 0), (0, 0, 128))
            # botao Garage
            if 325 <= mouse[0] <= 325 + 10 and 535 <= mouse[1] <= 535 + 10:
                mqttc.publish("smartElevator/elevatorTrips",
                              payload=str(whereTheElevatorIs) + ";" + str(-1) + ";" + str(whereTheElevatorIs) + ";"
                              + str(time.strftime('%X %x')) + ";" + str(elevatorWeight) + ";Building1"";",
                              qos=0)
                if whereTheElevatorIs > -1:
                    for index in range(whereTheElevatorIs - (-1)):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()

            if 395 <= mouse[0] <= 395 + 10 and 415 <= mouse[1] <= 415 + 10 and elevatorWeight + person1Weight <= 300:
                person0Status = True
                reload_all()
                elevatorWeight = elevatorWeight + person1Weight
                text2 = font.render('Weight: ' + str(elevatorWeight), True, (0, 255, 0), (0, 0, 128))
                pygame.time.wait(800)
                if whereTheElevatorIs > 0:
                    for index in range(whereTheElevatorIs - 0):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()
                if whereTheElevatorIs < 0:
                    for index in range(0 - whereTheElevatorIs):
                        yElevator -= 90
                        pygame.time.wait(100)
                        reload_all()
                person0Status = False

            if 395 <= mouse[0] <= 395 + 10 and 535 <= mouse[1] <= 535 + 10 and elevatorWeight + personGarWeight <= 300:
                personGarStatus = True
                reload_all()
                elevatorWeight = elevatorWeight + personGarWeight
                text2 = font.render('Weight: ' + str(elevatorWeight), True, (0, 255, 0), (0, 0, 128))
                pygame.time.wait(800)
                if whereTheElevatorIs > -1:
                    for index in range(whereTheElevatorIs - (-1)):
                        yElevator += 90
                        pygame.time.wait(100)
                        reload_all()
                personGarStatus = False

        if event.type == pygame.QUIT:
            running = False

    if yElevator >= 450:
        text = font.render('Elevator at Garage', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = -1

    if 360 <= yElevator < 450:
        text = font.render('Elevator at Floor 0', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = 0

    if 270 <= yElevator < 360:
        text = font.render('Elevator at Floor 1', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = 1

    if 180 <= yElevator < 270:
        text = font.render('Elevator at Floor 2', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = 2

    if 90 <= yElevator < 180:
        text = font.render('Elevator at Floor 3', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = 3

    if 0 <= yElevator < 90:
        text = font.render('Elevator at Floor 4', True, (0, 255, 0), (0, 0, 128))
        whereTheElevatorIs = 4

    reload_all()
