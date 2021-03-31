package io.github.paulushcgcj.mentorship.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import io.github.paulushcgcj.mentorship.models.company.Company;

public class CompanyValidator implements Validator {
  @Override
  public boolean supports(Class<?> aClass) {
    return Company.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","name.empty");
    Company company = (Company)o;
    if(company.getEmployees() == null || company.getEmployees() == 0)
      errors.rejectValue("employees","employees.nullOrEmpty");

  }
}
