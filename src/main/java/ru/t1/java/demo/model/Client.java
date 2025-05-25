package ru.t1.java.demo.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client extends AbstractPersistable<Long> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "client_id", unique = true, updatable = false)
    private UUID clientId = UUID.randomUUID();// автогенерация UUID

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    public Client(String firstName, String lastName, String middleName, UUID clientId, List<Account> accounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.clientId = clientId;
        this.accounts = accounts;
    }

    public Client() {
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public static class ClientBuilder {
        private String firstName;
        private String lastName;
        private String middleName;
        private UUID clientId;
        private List<Account> accounts;

        ClientBuilder() {
        }

        public ClientBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ClientBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ClientBuilder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public ClientBuilder clientId(UUID clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientBuilder accounts(List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public Client build() {
            return new Client(this.firstName, this.lastName, this.middleName, this.clientId, this.accounts);
        }

        public String toString() {
            return "Client.ClientBuilder(firstName=" + this.firstName + ", lastName=" + this.lastName + ", middleName=" + this.middleName + ", clientId=" + this.clientId + ", accounts=" + this.accounts + ")";
        }
    }
}