package app.model;

import java.util.Objects;

public final class Person implements Comparable<Person> {
    private final String name;
    private final int age;
    private final int id;

    private Person(Builder b) {
        this.name = b.name;
        this.age = b.age;
        this.id = b.id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Person other) {
        int cmp = Integer.compare(this.id, other.id);
        if (cmp != 0) return cmp;
        cmp = this.name.compareTo(other.name);
        if (cmp != 0) return cmp;
        return Integer.compare(this.age, other.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false; // pattern matching
        return age == person.age
                && id == person.id
                && java.util.Objects.equals(name, person.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, age, id);
    }

    @Override
    public String toString() {
        return "Person{id=" + id + ", name='" + name + "', age=" + age + "}";
    }

    public static class Builder {
        private String name;
        private int age;
        private int id;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Person build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("name must not be empty");
            }
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("age must be in [0..150]");
            }
            if (id < 0) {
                throw new IllegalArgumentException("id must be >= 0");
            }
            return new Person(this);
        }
    }
}
