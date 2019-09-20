package it.intre.conf.raffle.ux

import it.intre.conf.raffle.H
import it.intre.conf.raffle.PAD
import it.intre.conf.raffle.W
import javafx.scene.Parent
import javafx.scene.Scene

class RaffleScene(root: () -> Parent) : Scene(root(), 4 * W + 2 * PAD, 8 * H + 2 * PAD)