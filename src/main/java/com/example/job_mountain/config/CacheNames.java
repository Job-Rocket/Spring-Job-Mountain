package com.example.job_mountain.config;

/**
 * 위의 코드는 캐시 이름을 상수로 정의하는 클래스인 CacheNames를 나타냅니다. 각 상수는 특정 캐시 영역을 식별하는 문자열 값을 가지고 있습니다.
 * 이러한 캐시 이름 상수는 주로 Spring Cache와 같은 캐싱 기능을 사용할 때 캐시 이름을 지정하는 데 사용됩니다.
 * 예를 들어, @Cacheable 애노테이션에서 cacheNames 속성에 캐시 이름을 설정할 때 이러한 상수를 사용할 수 있습니다.
 *
 * 상수를 사용하여 캐시 이름을 정의하면, 캐시 영역을 구분하고 캐시에 저장된 데이터를 관리하기 쉬워집니다.
 * 또한, 캐시 이름을 상수로 정의함으로써 오타나 잘못된 캐시 이름 사용을 방지할 수 있습니다.
 */
public class CacheNames {
    public static final String USERBYUSERID = "CACHE_USERBYUSERID";
    public static final String ALLUSERS = "CACHE_ALLUSERS";
    public static final String LOGINUSER = "CACHE_LOGINUSER";

}
