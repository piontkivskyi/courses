package courses.domain.mappers

trait Mapper[T] {
  def map(value: T): String
}
