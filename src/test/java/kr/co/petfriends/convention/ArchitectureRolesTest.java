package kr.co.petfriends.convention;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Optional;
import kr.co.petfriends.sample.common.annotation.Adapter;
import kr.co.petfriends.sample.common.annotation.UseCase;
import org.hibernate.annotations.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class ArchitectureRolesTest extends ArchUnitSupport {

    @Nested
    @DisplayName("패키지 간 의존성을 검증한다.")
    class CheckPackageDependency {

        @Test
        @DisplayName("domain 패키지는 api, infrastructure 패키지에 의존하지 않아야 한다.")
        void checkDomainPackageDependency() {
            ArchRule rule = noClasses().that()
                .resideInAPackage(DEFAULT_PACKAGE + ".domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    DEFAULT_PACKAGE + ".api..",
                    DEFAULT_PACKAGE + ".infrastructure.."
                );

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("api 패키지는 infrastructure 패키지에 의존하지 않아야 한다.")
        void checkApiPackageDependency() {
            ArchRule rule = noClasses().that()
                .resideInAPackage(DEFAULT_PACKAGE + ".api..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    DEFAULT_PACKAGE + ".infrastructure.."
                );

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("infrastructure 패키지는 api 패키지에 의존하지 않아야 한다.")
        void checkInfrastructurePackageDependency() {
            ArchRule rule = noClasses().that()
                .resideInAPackage(DEFAULT_PACKAGE + ".infrastructure..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    DEFAULT_PACKAGE + ".api.."
                );

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("common 패키지는 domain, api, infrastructure 패키지에 의존하지 않아야 한다.")
        void checkCommonPackageDependency() {
            ArchRule rule = noClasses().that()
                .resideInAPackage(DEFAULT_PACKAGE + ".common..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                    DEFAULT_PACKAGE + ".domain..",
                    DEFAULT_PACKAGE + ".api..",
                    DEFAULT_PACKAGE + ".infrastructure.."
                );

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("패키지 간 순환 의존성이 없어야 한다.")
        void checkCyclicDependency() {
            ArchRule rule = slices().matching("..(*).(*)..")
                .should().beFreeOfCycles();

            rule.check(TARGET_CLASSES);
        }

//        @Test
//        @DisplayName("레이어드 아키텍처 레이어 규칙을 검증한다.")
//        void checkLayeredArchitectureRule() {
//            Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
//                .layer("Controller").definedBy(DEFAULT_PACKAGE + ".controller..")
//                .layer("Service").definedBy(DEFAULT_PACKAGE + ".service..")
//                .layer("Repository").definedBy(DEFAULT_PACKAGE + ".repository..")
//
//                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
//                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
//                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
//        }
//
//        @Test
//        @DisplayName("어니언 아키텍처(헥사고날 or 포트어댑터) 레이어 규칙을 검증한다.")
//        void checkOnionArchitectureRule() {
//            Architectures.onionArchitecture()
//                .domainModels(DEFAULT_PACKAGE + ".domain.model..")
//                .domainServices(DEFAULT_PACKAGE + ".domain.service..")
//                .applicationServices(DEFAULT_PACKAGE + ".application..")
//                .adapter("web", DEFAULT_PACKAGE + ".adapter.web..")
//                .adapter("dataaccess", DEFAULT_PACKAGE + ".adapter.dataaccess..")
//                .adapter("messaging", DEFAULT_PACKAGE + ".adapter.messaging..")
//                .adapter("external", DEFAULT_PACKAGE + ".adapter.external..");
//        }
    }

    @Nested
    @DisplayName("클래스 규칙을 검증한다.")
    class CheckClassConvention {

        @Test
        @DisplayName("UseCase 규칙을 검증한다.")
        void checkUseCaseRules() {
            ArchRule rule = classes().that()
                .resideInAnyPackage("..usecase")
                .should().haveSimpleNameEndingWith("UseCase")
                .andShould().beInterfaces()
                .because("usecase 패키지 내에 클래스명은 UseCase로 끝나야 한다.")
                .because("usecase 패키지 내에 클래스는 인터페이스여야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("UseCaseImpl 규칙을 검증한다.")
        void checkUseCaseImplRules() {
            ArchRule rule = classes().that()
                .areAnnotatedWith(UseCase.class)
                .should().haveSimpleNameEndingWith("UseCaseImpl")
                .andShould().notBeAnnotatedWith(Service.class)
                .andShould().notBeAnnotatedWith(Component.class)
                .andShould().bePackagePrivate()
                .because("@UseCase가 붙은 클래스명은 UseCaseImpl로 끝나야 한다.")
                .because("@UseCase가 붙은 클래스는 @Service 사용하지 않는다.")
                .because("@UseCase가 붙은 클래스는 @Compoent를 사용하지 않는다.")
                .because("@UseCase가 붙은 클래스는 package-private이어야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("Port 규칙을 검증한다.")
        void checkOutPortRules() {
            ArchRule rule = classes().that()
                .resideInAnyPackage("..port")
                .should().haveSimpleNameEndingWith("Port")
                .andShould().beInterfaces()
                .because("port 패키지 내에 클래스명은 Port로 끝나야 한다.")
                .because("port 패키지 내에 클래스는 인터페이스여야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("Adapter 규칙을 검증한다.")
        void checkAdapterRules() {
            ArchRule rule = classes().that()
                .areAnnotatedWith(Adapter.class)
                .should().haveSimpleNameEndingWith("Adapter")
                .andShould().notBeAnnotatedWith(Component.class)
                .andShould().notBeAnnotatedWith(Service.class)
                .andShould().bePackagePrivate()
                .because("@Adapter가 붙은 클래스명은 Adater로 끝나야 한다.")
                .because("@Adapter가 붙은 클래스는 @Compoent를 사용하지 않는다.")
                .because("@Adapter가 붙은 클래스는 @Service를 사용하지 않는다.")
                .because("@Adapter가 붙은 클래스는 package-private이어야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("Entity 규칙을 검증한다.")
        void checkEntityRules() {
            classes().that()
                .resideInAPackage("..entity").and()
                .haveNameNotMatching(".*\\$.*")  // 내부 클래스를 제외
                .should().haveSimpleNameEndingWith("Entity")
                .andShould().beAnnotatedWith(Entity.class)
                .andShould().beAnnotatedWith(Table.class)
                .because("entity 패키지 내에 클래스명는 Entity로 끝나야 한다.")
                .because("entity 패키지 내에 클래스는 @Entity를 사용해야 한다.")
                .because("entity 패키지 내에 클래스는 @Table를 사용해야 한다.")
                .check(TARGET_CLASSES);

            fields().that()
                .areAnnotatedWith(Column.class)
                .should().bePrivate()
                .andShould().beAnnotatedWith(Comment.class)
                .because("@Column이 붙은 모든 필드는 private이어야 한다.")
                .because("@Column이 붙은 모든 필드는 @Comment를 사용해야 한다.")
                .check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("Repository 규칙을 검증한다.")
        void checkRepositoryRules() {
            ArchRule rule = classes().that()
                .resideInAPackage("..repository")
                .should().haveSimpleNameEndingWith("Repository")
                .andShould().beInterfaces()
                .andShould().notBeAnnotatedWith(Repository.class)
                .because("repository 패키지 내에 클래스명는 Repository로 끝나야 한다.")
                .because("repository 패키지 내에 클래스는 인터페이스여야 한다.")
                .because("repository 패키지 내에 클래스는 @Repository를 사용하지 않아야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("Controller 규칙을 검증한다.")
        void checkControllerRules() {
            classes().that()
                .resideInAPackage("..controller")
                .should().haveSimpleNameEndingWith("Controller")
                .andShould().beAnnotatedWith(RestController.class)
                .andShould().notBeAnnotatedWith(Controller.class)
                .andShould().notBeAnnotatedWith(Component.class)
                .because("controller 패키지 내에 클래스명는 Controller로 끝나야 한다.")
                .because("controller 패키지 내에 클래스는 @RestController를 사용해야 한다.")
                .because("controller 패키지 내에 클래스는 @Controller를 사용하지 않아야 한다.")
                .because("controller 패키지 내에 클래스는 @Component를 사용하지 않아야 한다.")
                .check(TARGET_CLASSES);

            methods().that().arePublic().and()
                .areDeclaredInClassesThat().resideInAPackage("..controller")
                .should().haveRawReturnType(ResponseEntity.class)
                .because("controller 패키지 내에 public 메서드는 ResponseEntity를 리턴해야 한다.")
                .check(TARGET_CLASSES);

            methods().that().arePublic().and()
                .areDeclaredInClassesThat().resideInAPackage("..controller")
                .should().notBeAnnotatedWith(RequestMapping.class)
                .andShould().beAnnotatedWith(PostMapping.class)
                .orShould().beAnnotatedWith(GetMapping.class)
                .orShould().beAnnotatedWith(PutMapping.class)
                .orShould().beAnnotatedWith(DeleteMapping.class)
                .because("controller 패키지 내에 public 메서드는 @RequestMapping을 사용하지 않아야 한다.")
                .because(
                    "controller 패키지 내에 public 메서드는 @PostMapping, @GetMapping, @PutMapping, @DeleteMapping 중 하나를 사용해야 한다.")
                .check(TARGET_CLASSES);
        }
    }

    @Nested
    @DisplayName("코드 컨벤션을 검증한다.")
    class CheckCodingConvention {

        @Test
        @DisplayName("상수는 대문자로 작성해야 한다.")
        void checkConstantRules() {
            ArchRule rule = fields().that()
                .areDeclaredInClassesThat()
                .haveSimpleNameNotStartingWith("Q").and() // Q로 시작하는 클래스 제외
                .haveModifier(JavaModifier.PUBLIC).and()
                .haveModifier(JavaModifier.STATIC).and()
                .haveModifier(JavaModifier.FINAL)
                .should().haveNameMatching("^[A-Z0-9_]*$")
                .because("public static final 예약어가 붙은 필드는 대문자여야 한다.");

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("(findAll을 제외하고) find로 시작하는 메서드는 Optional 타입을 리턴해야 한다.")
        void checkMethodStartWithFindRules() {
            ArchRule rule = methods().that()
                .haveNameNotStartingWith("findAll").and()
                .haveNameStartingWith("find")
                .should().haveRawReturnType(Optional.class);

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("get으로 시작하는 메서드는 Optional 타입을 리턴하면 안된다.")
        void checkMethodStartWithGetRules() {
            ArchRule rule = methods().that()
                .haveNameStartingWith("get")
                .should().notHaveRawReturnType(Optional.class);

            rule.check(TARGET_CLASSES);
        }

        @Test
        @DisplayName("findAll으로 시작하는 메서드는 List 타입을 리턴해야 한다.")
        void checkMethodStartWithFindAllRules() {
            ArchRule rule = methods().that()
                .haveNameStartingWith("findAll")
                .should().haveRawReturnType(List.class);

            rule.check(TARGET_CLASSES);
        }
    }
}
