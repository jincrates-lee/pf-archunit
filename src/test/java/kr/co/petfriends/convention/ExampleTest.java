package kr.co.petfriends.convention;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.DomainModel;
import kr.co.petfriends.sample.common.annotation.UseCase;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class ExampleTest {

    private final String DEFAULT_PACKAGE = "kr.co.petfriends.sample";

    private final JavaClasses IMPORTED_CLASSES = new ClassFileImporter()
        .importPackages(DEFAULT_PACKAGE);  // 지정한 루트 패키지 이하의 모든 클래스를 대상으로 지정

    @Test
    @DisplayName("domain은 application과 adapter에서만 접근해야 한다.")
    void domains_should_only_be_accessed_by_application_and_adapter() {
        // 패키지를 지정하여 rule을 검증할 클래스 가져오기
        final JavaClasses IMPORTED_CLASSES = new ClassFileImporter().importPackages("kr.co.petfriends.sample");

        ArchRule rule = classes().that()       // classes() 메서드를 사용하여 클래스 규칙을 정의할 수 있습니다.
            .resideInAPackage("..domain..")    // 규칙: 패키지명이 domain로 끝나는 클래스는 application, infrastructure, domain 패키지에서만 접근해야 한다.
            .should().onlyBeAccessed().byAnyPackage(
                "..application..",
                "..adapter..",
                "..domain.."
            );

        rule.check(IMPORTED_CLASSES);  // check() 메서드를 사용하여 규칙을 검증할 수 있습니다.
    }

    @Test
    @DisplayName("domain 패키지는 application, adapter 패키지에 의존하지 않아야 한다.")
    void domain_should_not_depend_on_application_and_adapter() {
        ArchRule rule = noClasses().that()
            .resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "..application..",
                "..adapter.."
            );

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("api 패키지는 infrastructure 패키지에 의존하지 않아야 한다.")
    void api_should_not_depend_on_infrastructure() {
        ArchRule rule = noClasses().that()
            .resideInAPackage("..api..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("infrastructure 패키지는 api 패키지에 의존하지 않아야 한다.")
    void infrastructure_should_not_depend_on_api() {
        ArchRule rule = noClasses().that()
            .resideInAPackage("..infrastructure..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..api..");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("common 패키지는 domain, api, infrastructure 패키지에 의존하지 않아야 한다.")
    void common_should_not_depend_on_domain_and_api_and_infrastructure() {
        ArchRule rule = noClasses().that()
            .resideInAPackage("..common..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "..application..",
                "..infrastructure..",
                "..domain.."
            );

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("패키지 간 순환 의존성이 없어야 한다.")
    void no_cycles_between_packages() {
        ArchRule rule = slices().matching("..(*)..")
            .should().beFreeOfCycles();

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("도메인 패키지 내에서 순환 의존성이 없어야 한다.")
    void no_cycles_in_domains() {
        ArchRule rule = slices().matching("..(domain).(*)..")
            .should().beFreeOfCycles();

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("UseCaseImpl 규칙을 검증한다.")
    void usecase_impl_should_be_annotated_with_usecase() {
        ArchRule rule = classes().that()
            .areAnnotatedWith(UseCase.class)
            .should().notBeAnnotatedWith(Service.class)
            .andShould().notBeAnnotatedWith(Component.class)
            .because("@UseCase가 붙은 클래스는 @Service를 사용하면 안 된다.")
            .because("@UseCase가 붙은 클래스는 @Component를 사용하면 안 된다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Nested
    @DisplayName("Entity 규칙을 검증한다.")
    class CheckEntityRule {

        @Test
        @DisplayName("Entity 클래스는 @Entity와 @Table을 사용해야 한다.")
        void entities_should_be_annotated_with_entity() {
            ArchRule rule = classes().that()
                .areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(Table.class)
                .andShould().notBeAnnotatedWith(Setter.class)
                .because("entity 패키지 내에 클래스는 @Entity를 사용해야 한다.")
                .because("entity 패키지 내에 클래스는 @Table를 사용해야 한다.")
                .because("entity 패키지 내에 클래스는 @Setter를 사용하면 안 된다.");

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("@Column 어노테이션이 붙은 필드는 private이어야 하고, @Comment를 사용해야 한다.")
        void entities_should_be_annotated_with_comment() {
            ArchRule rule = fields().that()
                .areAnnotatedWith(Column.class)
                .should().bePrivate()
                .andShould().beAnnotatedWith(Comment.class)
                .because("@Column이 붙은 모든 필드는 private이어야 한다.")
                .because("@Column이 붙은 모든 필드는 @Comment를 사용해야 한다.");

            rule.check(IMPORTED_CLASSES);
        }
    }

    @Test
    @DisplayName("domain.model 클래스는 이름이 Order로 시작해야 한다.")
    void order_model_should_be_start_with_order() {
        ArchRule rule = classes().that()
            .resideInAnyPackage("..domain.model..")
            .should().haveSimpleNameStartingWith("Order")
            .because("domain.model 패키지 내에 클래스명은 Order로 시작해야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("Port는 이름이 Port로 끝나야하고 인터페이스여야 한다.")
    void port_should_be_interface_and_end_with_port() {
        ArchRule rule = classes().that()
            .resideInAnyPackage("..port..")
            .should().haveSimpleNameEndingWith("Port")
            .andShould().beInterfaces()
            .because("port 패키지 내에 클래스명은 Port로 끝나야 한다.")
            .because("port 패키지 내에 클래스는 인터페이스여야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("어니언 아키텍처(헥사고날 or 포트어댑터) 레이어 규칙을 검증한다.")
    void checkOnionArchitectureRule() {
        final String DEFAULT_PACKAGE = "kr.co.petfriends.sample";

        onionArchitecture()
            .domainModels(DEFAULT_PACKAGE + ".domain.model..")
            .domainServices(DEFAULT_PACKAGE + ".domain.service..")
            .applicationServices(DEFAULT_PACKAGE + ".application..")
            .adapter(
                "web",
                DEFAULT_PACKAGE + ".adapter.web.."
            )
            .adapter(
                "dataaccess",
                DEFAULT_PACKAGE + ".adapter.dataaccess.."
            )
            .adapter(
                "messaging",
                DEFAULT_PACKAGE + ".adapter.messaging.."
            )
            .adapter(
                "external",
                DEFAULT_PACKAGE + ".adapter.external.."
            );
    }

    @Test
    @DisplayName("(findAll을 제외하고) find로 시작하는 메서드는 Optional 타입을 리턴해야 한다.")
    void find_method_should_return_optional() {
        ArchRule rule = methods().that()
            .haveNameNotStartingWith("findAll").and()
            .haveNameStartingWith("find")
            .should().haveRawReturnType(Optional.class);

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("get으로 시작하는 메서드는 Optional 타입을 리턴하면 안 된다.")
    void get_method_should_not_return_optional() {
        ArchRule rule = methods().that()
            .haveNameStartingWith("get")
            .should().notHaveRawReturnType(Optional.class);

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("상수는 대문자로 작성해야 한다.")
    void constants_should_be_uppercase() {
        ArchRule rule = fields().that()
            .areDeclaredInClassesThat()
            .haveSimpleNameNotStartingWith("Q").and() // Q로 시작하는 클래스 제외
            .arePublic().and()
            .areStatic().and()
            .areFinal()
            .should().haveNameMatching("^[A-Z0-9_]*$")
            .because("public static final 예약어가 붙은 필드는 대문자여야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("controller 패키지 내에 public 메서드는 ResponseEntity를 리턴해야 한다")
    void controller_should_return_response_entity() {
        ArchRule rule = methods().that()
            .arePublic().and()
            .areDeclaredInClassesThat().resideInAPackage("..controller..")
            .should().haveRawReturnType(ResponseEntity.class)
            .because("controller 패키지 내에 public 메서드는 ResponseEntity를 리턴해야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("Service는 @Transactional을 사용해야 한다.")
    void service_should_be_annotated_with_transactional() {
        ArchRule rule = classes().that()
            .resideInAPackage("..service..")
            .should().beAnnotatedWith(Transactional.class)
            .because("service 패키지 내에  클래스는 @Transactional을 사용해야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("UseCase 구현체는 @Transactional을 사용하면 안 된다.")
    void usecase_should_not_be_annotated_with_transactional() {
        ArchRule rule = classes().that()
            .resideInAPackage("..usecase.impl..")
            .should().notBeAnnotatedWith(Transactional.class)
            .because("usecase.impl 패키지 내에  클래스는 @Transactional을 사용하면 안 된다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("getAll로 시작하는 메서드는 List 타입을 리턴해야 한다.")
    void get_all_method_should_return_list() {
        ArchRule rule = methods().that()
            .haveNameStartingWith("getAll")
            .should().haveRawReturnType(List.class);

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("DomainModel은 record여야 한다.")
    void domain_model_should_be_record() {
        ArchRule rule = classes().that()
            .areAnnotatedWith(DomainModel.class)
            .should().beRecords()
            .because("@DomainModel가 붙은 클래스는 record여야 한다.");

        rule.check(IMPORTED_CLASSES);
    }
}
