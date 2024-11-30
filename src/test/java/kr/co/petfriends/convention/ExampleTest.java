package kr.co.petfriends.convention;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
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
    @DisplayName("domain은 service와 repository에서만 접근해야 한다.")
    void domains_should_only_be_accessed_by_service_and_repository() {
        // 패키지를 지정하여 rule을 검증할 클래스 가져오기
        final JavaClasses IMPORTED_CLASSES = new ClassFileImporter().importPackages("kr.co.petfriends.sample");

        ArchRule rule = classes().that()       // classes() 메서드를 사용하여 클래스 규칙을 정의할 수 있습니다.
            .resideInAPackage("..domain..").and()    // 규칙: 패키지명이 domain로 끝나는 클래스는 service, repository, domain 패키지에서만 접근해야 한다.
            .haveNameNotMatching(".*\\$.*")  // 내부 클래스를 제외
            .should().onlyBeAccessed().byAnyPackage(
                "..service..",
                "..repository..",
                "..domain.."
            );

        rule.check(IMPORTED_CLASSES);  // check() 메서드를 사용하여 규칙을 검증할 수 있습니다.
    }



    @Nested
    @DisplayName("패키지 간 의존 관계를 검증한다.")
    class CheckPackageDependencyRule {
        @Test
        @DisplayName("패키지 간 순환 의존성이 없어야 한다.")
        void no_cycles_between_packages() {
            ArchRule rule = slices().matching("..(sample).(*)..").namingSlices("$2 of $1")
                .should().beFreeOfCycles();

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("domain 패키지는 controller, service, repository 패키지에 의존하지 않아야 한다.")
        void domain_should_not_depend_on_controller_and_service_and_repository() {
            ArchRule rule = noClasses().that()
                .resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..controller..",
                    "..service..",
                    "..repository.."
                );

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("controller 패키지는 domain, repository 패키지에 의존하지 않아야 한다.")
        void controller_should_not_depend_on_domain_and_repository() {
            ArchRule rule = noClasses().that()
                .resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..domain..",
                    "..repository.."
                );

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("service 패키지는 controller 패키지에 의존하지 않아야 한다.")
        void service_should_not_depend_on_controller() {
            ArchRule rule = noClasses().that()
                .resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..controller..");

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("repository 패키지는 controller, service 패키지에 의존하지 않아야 한다.")
        void repository_should_not_depend_on_controller_and_service() {
            ArchRule rule = noClasses().that()
                .resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    "..controller..",
                    "..service.."
                );

            rule.check(IMPORTED_CLASSES);
        }
    }

    @Nested
    @DisplayName("어노테이션 규칙을 검증한다.")
    class CheckAnnotationRule {
        @Test
        @DisplayName("Service는 @Transactional을 사용해야 한다.")
        void service_should_be_annotated_with_transactional() {
            ArchRule rule = classes().that()
                .resideInAPackage("..service").and()
                .areNotInterfaces()
                .should().beAnnotatedWith(Transactional.class)
                .because("service 패키지 내에  (인터페이스가 아닌) 클래스는 @Transactional을 사용해야 한다.");

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("Repository는 @Transactional을 사용하면 안 된다.")
        void repository_should_not_be_annotated_with_transactional() {
            ArchRule rule = classes().that()
                .resideInAPackage("..repository..")
                .should().notBeAnnotatedWith(Transactional.class)
                .because("repository 패키지 내에  클래스는 @Transactional을 사용하면 안 된다.");

            rule.check(IMPORTED_CLASSES);
        }
    }

    @Test
    @DisplayName("UseCase 구현체는 @Service, @Compoent가 아닌 @UseCase을 사용해야 한다.")
    void usecase_impl_should_be_annotated_with_usecase() {
        ArchRule rule = classes().that()
            .areAnnotatedWith(UseCase.class)  // 특정 어노테이션이 붙은 클래스 필터링
            .should().notBeAnnotatedWith(Service.class)
            .andShould().notBeAnnotatedWith(Component.class)
            .because("@UseCase가 붙은 클래스는 @Service를 사용하면 안 된다.")
            .because("@UseCase가 붙은 클래스는 @Component를 사용하면 안 된다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("domain model은 record여야 한다.")
    void domain_model_should_be_record() {
        ArchRule rule = classes().that()
            .areAnnotatedWith(DomainModel.class)
            .should().beRecords()
            .because("@DomainModel가 붙은 클래스는 record여야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Nested
    @DisplayName("Entity 규칙을 검증한다.")
    class CheckEntityRule {

        @Test
        @DisplayName("Entity 클래스는 @Entity와 @Table을 사용해야 한다.")
        void entitiy_should_be_annotated_with_entity() {
            ArchRule rule = classes().that()
                .areAnnotatedWith(Entity.class)
                .should().beAnnotatedWith(Table.class)
                .because("entity 패키지 내에 클래스는 @Entity를 사용해야 한다.")
                .because("entity 패키지 내에 클래스는 @Table를 사용해야 한다.");

            rule.check(IMPORTED_CLASSES);
        }

        @Test
        @DisplayName("@Column 어노테이션이 붙은 필드는 private이어야 하고, @Comment를 사용해야 한다.")
        void entitiy_should_be_annotated_with_comment() {
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
    @DisplayName("order.domain.model 클래스는 이름이 Order로 시작해야 한다.")
    void order_model_should_be_start_with_order() {
        ArchRule rule = classes().that()
            .resideInAnyPackage("..order.domain.model..")
            .should().haveSimpleNameStartingWith("Order")
            .because("order.domain.model 패키지 내에 클래스명은 Order로 시작해야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("Repository는 이름이 Repository로 끝나야 하고, 인터페이스여야 한다.")
    void repository_should_be_interface_and_end_with_repository() {
        ArchRule rule = classes().that()
            .resideInAnyPackage("..repository")
            .should().haveSimpleNameEndingWith("Repository")
            .andShould().beInterfaces()
            .because("repository 패키지 내에 클래스명은 Repository로 끝나야 한다.")
            .because("repository 패키지 내에 클래스는 인터페이스여야 한다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("레이어드 아키텍처 레이어 규칙을 검증한다.")
    void check_layered_architecture_rule() {
        ArchRule rule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")

            .because("Controller는 다른 레이어에서 접근하지 않아야 한다.")
            .because("Service는 Controller와 Service 레이어에서만 접근할 수 있다.")
            .because("Repository는 Service 레이어에서만 접근할 수 있다.");

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("어니언 아키텍처(헥사고날 or 포트어댑터) 레이어 규칙을 검증한다.")
    void check_onion_architecture_rule() {
        ArchRule rule = onionArchitecture()
            .domainModels("..domain.model..")
            .domainServices("..domain.service..")
            .applicationServices("..application..")
            .adapter("web", "..adapter.web..")
            .adapter("dataaccess", "..adapter.dataaccess..")
            .adapter("messaging", "..adapter.messaging..")
            .adapter("external", "..adapter.external..");

        rule.check(IMPORTED_CLASSES);
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
    @DisplayName("getAll로 시작하는 메서드는 List 타입을 리턴해야 한다.")
    void get_all_method_should_return_list() {
        ArchRule rule = methods().that()
            .haveNameStartingWith("getAll")
            .should().haveRawReturnType(List.class);

        rule.check(IMPORTED_CLASSES);
    }

    @Test
    @DisplayName("controller 패키지 내에 public 메서드는 ResponseEntity를 리턴해야 한다.")
    void controller_should_return_response_entity() {
        ArchRule rule = methods().that()
            .areDeclaredInClassesThat().resideInAPackage("..controller..").and()
            .arePublic()
            .should().haveRawReturnType(ResponseEntity.class)
            .because("controller 패키지 내에 public 메서드는 ResponseEntity를 리턴해야 한다.");

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
}
