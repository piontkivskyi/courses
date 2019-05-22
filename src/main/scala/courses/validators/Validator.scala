package courses.validators

/**
  * Implement validator typeclass that should validate arbitrary value [T].
  * @tparam T the type of the value to be validated.
  */
trait Validator[T] {
  /**
    * Validates the value.
    * @param value value to be validated.
    * @return Right(value) in case the value is valid, Left(message) on invalid value
    */
  def validate(value: T): Either[String, T]

  /**
    * And combinator.
    * @param other validator to be combined with 'and' with this validator.
    * @return the Right(value) only in case this validator and <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from the validator that failed.
    */
  def and(other: Validator[T]): Validator[T] = {
    val oldValidator = this
    new Validator[T] {
      override def validate(value: T): Either[String, T] = {
        oldValidator.validate(value) match {
          case Right(_) => other.validate(value)
          case Left(err) => Left(err)
        }
      }
    }
  }

  /**
    * Or combinator.
    * @param other validator to be combined with 'or' with this validator.
    * @return the Right(value) only in case either this validator or <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from both courses.validators.
    */
  def or(other: Validator[T]): Validator[T] = {
    val oldValidator = this
    new Validator[T] {
      override def validate(value: T): Either[String, T] = {
        oldValidator.validate(value) match {
          case Left(_) => other.validate(value)
          case Right(result) => Right(result)
        }
      }
    }
  }
}


object Validator {
  val positiveInt : Validator[Int] = new Validator[Int] {
    override def validate(t: Int): Either[String, Int] = {
      if (t > 0) {
        Right(t)
      } else {
        Left("Should be positive int")
      }
    }
  }

  def lessThan(n: Int): Validator[Int] = new Validator[Int] {
    override def validate(t: Int): Either[String, Int] = {
      if (n > t) {
        Right(t)
      } else {
        Left("Value should be less then " + n)
      }
    }
  }

  val nonEmpty : Validator[String] = new Validator[String] {
    override def validate(t: String): Either[String, String] =
      if (t.nonEmpty) { Right(t) }
      else { Left("Should be non empty string") }
  }

  val onlyAlphanumericCharacters: Validator[String] = new Validator[String] {
    /**
      * Validates the value.
      *
      * @param value value to be validated.
      * @return Right(value) in case the value is valid, Left(message) on invalid value
      */
    override def validate(value: String): Either[String, String] =
      if (value.forall(_.isLetterOrDigit)) { Right(value) }
      else { Left("Should contain only alphanumeric characters") }
  }

  val isPersonValid = new Validator[Person] {
    // Returns valid only when the name is not empty and age is in range [1-99].
    override def validate(value: Person): Either[String, Person] = {
      if (value.name.nonEmpty && (value.age >= 1 && value.age <= 99)) {
        Right(value)
      } else {
        Left("Person should have non empty name and age in range [1-99]")
      }
    }
  }

  implicit val intValidator: Validator[Int] = Validator.positiveInt
  implicit val stringValidator: Validator[String] = Validator.nonEmpty
  implicit val personValidator: Validator[Person] = Validator.isPersonValid

  implicit class ValidatableObject[T](val s: T) {
    def validate(implicit validator: Validator[T]): Either[String, T] = validator.validate(s)
  }
}

object ValidApp {
  import Validator._

  // uncomment make possible next code to compile
  2 validate (positiveInt and lessThan(10))

  // uncomment make possible next code to compile
  "" validate Validator.nonEmpty

  // uncomment make possible next code to compile
  Person(name = "John", age = 25) validate isPersonValid
}

object ImplicitValidApp {
  import Validator._

  // uncomment next code and make it compilable and workable
  Person(name = "John", age = 25).validate
  "asdasd".validate
  234.validate
}


case class Person(name: String, age: Int)
