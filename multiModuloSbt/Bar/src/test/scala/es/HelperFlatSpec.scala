package es

import org.scalatest.{FlatSpec, Matchers}

class HelperFlatSpec extends FlatSpec with Matchers {

  "Normalizar" should " valer " in {
    es.Helper.normalizar01(12d, 98d, 73d) should be(0.7093023255813954)
  }
  it should "000" in {
    es.Helper.normalizar01(0, 1, 0) should be(0)
  }
}
