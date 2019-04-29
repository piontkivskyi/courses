package validators

import org.scalatest.FlatSpec

class ValidatorTest extends FlatSpec {
  import Validator._

  "Validator " should "validate positive int" in {
    val positiveIntValidator = Validator.positiveInt

    assert(positiveIntValidator.validate(1) == Right(1))

    assert(positiveIntValidator.validate(0) == Left("Should be positive int"))
  }

  it should "validate nonEmpty string" in {
    val nonEmptyStringValidator = Validator.nonEmpty

    assert(nonEmptyStringValidator.validate("test") == Right("test"))

    assert(nonEmptyStringValidator.validate("") == Left("Should be non empty string"))
  }

  it should "validate numbers less then given" in {
    val lessThenValidator = Validator.lessThan(5)

    assert(lessThenValidator.validate(4) == Right(4))

    assert(lessThenValidator.validate(6) == Left("Value should be less then 5"))
  }

  it should "validate Person" in {
    val personValidator = Validator.isPersonValid

    val validPerson = Person("Test", 10)

    assert(personValidator.validate(validPerson) == Right(validPerson))

    assert(personValidator.validate(Person("", 0)) == Left("Person should have non empty name and age in range [1-99]"))
  }

  it should "be able to chain multiple validators by using and" in {

  }

  it should "be able to chain multiple validators by using or" in {

  }

  it should "have validate method in int type" in {
    val positiveResult = 1 validate Validator.positiveInt
    assert(positiveResult == Right(1))

    val negativeResult = -1 validate Validator.positiveInt

    assert(negativeResult == Left("Should be positive int"))
  }

  it should "have validate methed in string type" in {
    val positiveResult = "test" validate Validator.nonEmpty

    assert(positiveResult == Right("test"))

    val negativeResult = "" validate Validator.nonEmpty
    assert(negativeResult == Left("Should be non empty string"))
  }

  it should "have validate method in Person class" in {
    val validPerson = Person("Test", 10)
    val positiveResult = validPerson validate Validator.isPersonValid

    assert(positiveResult == Right(validPerson))

    val nonValidPerson = Person("", 0)
    val negativeResult = nonValidPerson validate Validator.isPersonValid

    assert(negativeResult == Left("Person should have non empty name and age in range [1-99]"))
  }

  it should "have default validator for each type" in {
    val positiveStringResult = "test".validate
    assert(positiveStringResult == Right("test"))

    val positiveIntResult = 10.validate
    assert(positiveIntResult == Right(10))

    val positivePersonResult = Person("Test", 10).validate
    assert(positivePersonResult == Right(Person("Test", 10)))
  }
}
