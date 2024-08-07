package swp.internmanagement.internmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import swp.internmanagement.internmanagement.models.UserAccount;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "company_name", nullable = false, length = 50)
    private String companyName;

    @NotNull
    @Nationalized
    @Lob
    @Column(name = "company_description", nullable = false)
    private String companyDescription;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "location", nullable = false, length = 50)
    private String location;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserAccount> userAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();
}