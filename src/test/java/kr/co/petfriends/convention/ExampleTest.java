package kr.co.petfriends.convention;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.Predefined;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    private final String DEFAULT_PACKAGE = "kr.co.petfriends.sample";
    private final String DOMAIN_PACKAGE_SUB = DEFAULT_PACKAGE + ".domain..";
    private final String API_PACKAGE_SUB = DEFAULT_PACKAGE + ".api..";
    private final String INFRA_PACKAGE_SUB = DEFAULT_PACKAGE + ".infrastructure..";

    /**
     * 검사 대상 클래스 파일 설정
     */
    private final JavaClasses TARGET_CLASSES = new ClassFileImporter()
        .withImportOption(Predefined.DO_NOT_INCLUDE_TESTS)  // 테스트 코드는 제외
        .withImportOption(Predefined.DO_NOT_INCLUDE_ARCHIVES)  // 아카이브 파일은 제외
        .withImportOption(Predefined.DO_NOT_INCLUDE_JARS)  // JAR 파일은 제외
        .importPackages(DEFAULT_PACKAGE);  // 지정한 루트 패키지 이하의 모든 클래스를 대상으로 지정

    @Test
    @DisplayName("domain 패키지는 api, infrastructure 패키지에 의존하지 않아야 한다.")
    void checkDomainPackageDependency() {
        // 검증하고자 하는 rule을 작성합니다.
        ArchRule rule = noClasses().that()  // 특정 조건에 맞는 클래스가 없어야 한다.
            .resideInAPackage(DOMAIN_PACKAGE_SUB)  // DOMAIN_PACKAGE_SUB 패키지에 속한 클래스 중에서
            .should().dependOnClassesThat()  // 의존하는 클래스가 있다면
            .resideInAnyPackage(  // 다음 패키지 중 하나라도 속한
                API_PACKAGE_SUB,
                INFRA_PACKAGE_SUB
            );

        // DOMAIN_PACKAGE_SUB 패키지에 속한 클래스 중에서
        // API_PACKAGE_SUB, INFRA_PACKAGE_SUB 패키지에 속한 클래스에 의존하는 클래스가 없어야 한다.
        rule.check(TARGET_CLASSES);
    }

    @Test
    @DisplayName("패키지 간 순환 의존성이 없어야 한다.")
    void checkCyclicDependency() {
        ArchRule rule = slices().matching(DEFAULT_PACKAGE + ".(*)..")
            .should().beFreeOfCycles();

        rule.check(TARGET_CLASSES);
    }
}
