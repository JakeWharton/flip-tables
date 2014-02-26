package com.jakewharton.fliptables.util;

public enum PersonType {
  HUMAN("Human"),
  COSTUME("Costume"),
  PUPPET("Puppet");

  private final String value;

  PersonType(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return value;
  }
}
