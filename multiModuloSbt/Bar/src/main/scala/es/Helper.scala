package es


import scala.reflect.ClassTag

object Helper {

  def normalizarGeneral(min: Double, max: Double, valor: Double, minNuevo: Double = 0, maxNuevo: Double = 1): Double = {
    ((valor - min) / (max - min)) * (maxNuevo - minNuevo) + minNuevo
  }

  def normalizar01(min: Double, max: Double, valor: Double): Double = {
    normalizarGeneral(min, max, valor)
  }

  def normalizarZ(media: Double, desv: Double, valor: Double): Double = {
    (valor - media) / desv
  }
}
