package com.jakewharton.fliptables;

public final class Person {
  private final String firstName;
  private final String lastName;
  private final int age;
  private final String nickName;

  public Person(String firstName, String lastName, int age, String nickName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.nickName = nickName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getAge() {
    return age;
  }

  public String getNickName() {
    return nickName;
  }
}
