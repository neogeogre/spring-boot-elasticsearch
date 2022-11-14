package com.demo.elasticsearch

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class DocController {

  @GetMapping(value = ["/"])
  fun redirectToDocPage(): ModelAndView {
    return ModelAndView("redirect:/swagger-ui.html")
  }

  @GetMapping(value = ["/apidocs"])
  fun redirectToApiPage(): ModelAndView {
    return ModelAndView("redirect:/swagger-ui.html")
  }

}