package kr.co.petfriends.convention;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.Predefined;

public abstract class ArchUnitSupport {

    protected final String DEFAULT_PACKAGE = "kr.co.petfriends.sample";

    /**
     * 검사 대상 클래스 파일 설정
     */
    protected final JavaClasses TARGET_CLASSES = new ClassFileImporter()
        .withImportOption(Predefined.DO_NOT_INCLUDE_TESTS)  // 테스트 코드는 제외
        .withImportOption(Predefined.DO_NOT_INCLUDE_ARCHIVES)  // 아카이브 파일은 제외
        .withImportOption(Predefined.DO_NOT_INCLUDE_JARS)  // JAR 파일은 제외
        .importPackages(DEFAULT_PACKAGE);  // 지정한 루트 패키지 이하의 모든 클래스를 대상으로 지정
}
