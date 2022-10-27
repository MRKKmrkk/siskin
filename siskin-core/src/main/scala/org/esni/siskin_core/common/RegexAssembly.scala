package org.esni.siskin_core.common

trait RegexAssembly {

  protected def isMatch(content: String, regex: String): Boolean = {

    val maybeMatch = regex.r findFirstMatchIn (content)
    maybeMatch.isDefined

  }

}
