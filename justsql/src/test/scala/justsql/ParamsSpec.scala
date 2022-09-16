/*
 * Copyright 2022 Simer JS Plaha (simer.j@gmail.com - @simerplaha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package justsql

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class ParamsSpec extends AnyWordSpec {

  "?" should {
    "accumulate parameters" when {
      "single params" in {
        val params: Params = Params()

        params ? 1 shouldBe "?"
        params ? 2 shouldBe "?"
        params ? "three" shouldBe "?"
        params ? true shouldBe "?"

        params.parameters().map(_.value) shouldBe Array(1, 2, "three", true)
      }

      "tuple params" in {
        val params: Params = Params()

        params ? (1, 2) shouldBe "?, ?"
        params ? (3, 4, true) shouldBe "?, ?, ?"
        params ? (5, false, 6, "three") shouldBe "?, ?, ?, ?"

        params.parameters().map(_.value) shouldBe
          Array(
            (1, 2),
            (3, 4, true),
            (5, false, 6, "three")
          )
      }

      "iterable params" in {
        val params: Params = Params()

        params ? List(1, 2) shouldBe "?, ?"
        params ? List(3, 4) shouldBe "?, ?"

        params.parameters().map(_.value) shouldBe Array(1, 2, 3, 4)
      }
    }
  }

  "embed" should {
    "embed query with no parameters" in {
      val sql1 =
        SelectSQL[Int] {
          "SELECT * FROM TABLE"
        }

      val sql2 =
        SelectSQL[Int] {
          implicit param: Params =>
            s"SELECT * FROM TABLE WHERE column1 = ${1.?} and column2 = (${sql1.embed})"
        }
      //parameters are merged for the second query
      sql2.params.parameters().map(_.value) should contain only 1

      //sql1 params are unchanged
      sql1.params.parameters().map(_.value) shouldBe empty
    }

    "merge parameter" in {
      val sql1 =
        SelectSQL[Int] {
          implicit param: Params =>
            s"SELECT * FROM TABLE WHERE column1 = ${1.?} AND column2 = ${"Wassup!".?}"
        }

      val sql2 =
        SelectSQL[Int] {
          implicit param: Params =>
            s"SELECT * FROM TABLE WHERE column1 = ${2.?} and column2 = (${sql1.embed})"
        }

      //parameters are merged for the second query
      sql2.params.parameters().map(_.value) shouldBe Array(2, 1, "Wassup!")

      //sql1 params are unchanged
      sql1.params.parameters().map(_.value) shouldBe Array(1, "Wassup!")
    }
  }

}
