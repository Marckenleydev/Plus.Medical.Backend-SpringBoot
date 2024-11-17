package marc.dev.DoctorBooking_appointement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.exception.ApiException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Auditable {
    @Id
    @SequenceGenerator(name = "primary_key_seq", sequenceName ="primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    @NotNull
    private Long createdBy;
    @NotNull
    private Long updatedBy;
    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @NotNull
    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public  void beforePersist(){
        var userId = 0L;
//        var  userId= RequestContext.getUserId();
//        if(userId == null) { throw new ApiException("Cannot persist entity without user ID in Request Context for this thread");}

        setCreatedAt(now());
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setUpdatedAt(now());
    }

    @PreUpdate
    public void beforeUpdate() {
        var userId = 0L;

//        var  userId= RequestContext.getUserId();
//        if(userId == null) { throw new ApiException("Cannot persist entity without user ID in Request Context for this thread");}

        setUpdatedAt(now());
        setUpdatedBy(userId);
    }




}
