package marc.dev.DoctorBooking_appointement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.entity.SpecialisationEntity;
import marc.dev.DoctorBooking_appointement.enumeration.Authority;
import marc.dev.DoctorBooking_appointement.repository.RoleRepository;
import marc.dev.DoctorBooking_appointement.repository.SpecialisationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@Slf4j
public class  Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RoleRepository roleRepository,SpecialisationRepository specialisationRepository) {
		return args -> {
			 RequestContext.setUserId(0L);

			// Create and save specializations
	/*		String[] specialisations = {"Cardiology", "Dermatology", "Neurology", "Pediatrics", "Psychiatry", "Radiology", "Surgery", "Urology"};
			for(String specialisation : specialisations){
				SpecialisationEntity specialisationEntity = new SpecialisationEntity();
				specialisationEntity.setName(specialisation);
				specialisationRepository.save(specialisationEntity);
			}



			var patientRole = new RoleEntity();
			patientRole.setName(Authority.PATIENT.name());
			patientRole.setAuthorities(Authority.PATIENT);
			roleRepository.save(patientRole);

			var doctorRole = new RoleEntity();
			doctorRole.setName(Authority.DOCTOR.name());
			doctorRole.setAuthorities(Authority.DOCTOR);
			roleRepository.save(doctorRole);
			RequestContext.start();

			var adminRole = new RoleEntity();
			adminRole.setName(Authority.ADMIN.name());
			adminRole.setAuthorities(Authority.ADMIN);
			roleRepository.save(adminRole);
			RequestContext.start();

			var staffRole = new RoleEntity();
			staffRole.setName(Authority.STAFF.name());
			staffRole.setAuthorities(Authority.STAFF);
			roleRepository.save(staffRole);
			RequestContext.start();

			 */
		};
	}


}
