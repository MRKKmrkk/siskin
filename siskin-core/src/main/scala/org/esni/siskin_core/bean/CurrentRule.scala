package org.esni.siskin_core.bean

case class CurrentRule(
                      filterRule: List[FilterRule],
                      IdentifiedRule: List[IdentifiedRule],
                      updateTimestamp: Long
                      )
