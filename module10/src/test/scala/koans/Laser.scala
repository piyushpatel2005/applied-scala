package lab.awesome.laser;
/* Copyright (C) 2010-2018 Escalate Software, LLC. All rights reserved. */

class Gun(wattage: Int) {
  class Beam (val lumens: Int)

  def shoot() = new Beam(10 * wattage)
}
