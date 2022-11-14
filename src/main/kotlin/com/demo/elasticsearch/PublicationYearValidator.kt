package com.demo.elasticsearch

import java.time.Year
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class PublicationYearValidator : ConstraintValidator<PublicationYear, Int> {

  override fun isValid(value: Int, context: ConstraintValidatorContext): Boolean {
    return !Year.of(value).isAfter(Year.now())
  }

}