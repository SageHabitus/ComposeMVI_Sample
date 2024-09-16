### 📚 책 검색 애플리케이션 (Jetpack Compose with Advanced MVI Concepts)


본 애플리케이션은 보일러 플레이트 코드가 많지만 의도한 것임을 알려드립니다. **Paging 3**와 **Room**, **Retrofit** 등의 최신 안드로이드 기술 스택을 활용하여 대규모 데이터 처리와 오프라인 저장소를 효율적으로 관리합니다. 본 애플리케이션은 효율적이고 일관된 사용자 경험을 제공하기 위해 설계되었고 최대한 구글아키텍쳐를 적용하기 위해 노력했습니다.

## 생략·병합 가능한 부분
**DataSource, UseCase 생략가능, DomainModel, PresentationModel 생략 가능, UiState 병합 가능**

## 주요 기능 ✨

- **실시간 API 통신 🌐**: 카카오톡 API 또는 네이버 API를 활용하여 사용자 요청에 따라 실시간으로 책 정보를 검색합니다. Retrofit과 OkHttp를 통해 안정적인 네트워크 통신을 보장합니다.
  
- **MVI 아키텍처 🧠**: MVI 패턴을 통해 모든 사용자 이벤트(Intent)를 중앙에서 처리하고, 이를 기반으로 UI 상태(State)가 변경되어 UI에 반영됩니다. MVI의 핵심 구성 요소인 **PartialStateChange**, **Intent**, **Event**, **State**는 상태 관리를 단순화하고, 이벤트 기반의 명확한 흐름을 유지합니다.

- **로컬 데이터 캐싱 🏠**: Room을 사용하여 검색된 책 데이터를 로컬에 저장하고 관리합니다. 네트워크 연결이 끊겨도 저장된 데이터를 통해 사용자 경험을 유지할 수 있습니다.

- **효율적인 페이징 처리 🔄**: Paging 3 라이브러리를 통해 대용량 데이터를 페이지 단위로 로드하여 성능을 최적화합니다. Jetpack Compose와 통합되어 무한 스크롤, 데이터 갱신 등의 기능을 부드럽게 처리합니다.

- **Jetpack Compose UI 📱**: 선언적 UI 프레임워크인 Jetpack Compose를 사용하여 Android UI를 간결하고 효율적으로 구성합니다. 코드 재사용성을 높이고, UI 업데이트의 복잡성을 줄입니다.

- **CI 자동화 🚀**: GitHub Actions를 활용해 Pull Request 시 자동 빌드 및 테스트를 수행합니다.

## 기술 스택 🛠️

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **네트워크 통신**: Retrofit, OkHttp, Corouitne, Flow
- **비동기 작업**: Corouitne, Flow, StateFlow, SharedFlow
- **JSON 파싱**: Kotlin Serialization, Parcelable
- **로컬 데이터베이스**: Room
- **이미지 로딩**: Coil
- **의존성 주입**: Hilt
- **페이징 처리**: Paging 3 (+RemoteMediator)
- **테스트**: JUnit, Espresso, Mokito, Compose UI Test
- **CI**: GitHub Actions

## MVI 아키텍처의 장점 및 MVVM의 한계 🚀

### MVVM의 한계
**MVVM (Model-View-ViewModel)** 아키텍처는 Android 개발에서 널리 사용되는 패턴이지만, 몇 가지 한계를 가지고 있습니다:
- **상태 관리의 복잡성**: ViewModel이 너무 많은 상태를 관리하게 되면서 코드가 복잡해질 수 있습니다.
- **UI와 비즈니스 로직의 강한 결합**: View와 ViewModel 간의 상호작용이 복잡해지면, 유지보수가 어려워질 수 있습니다.
- **비동기 이벤트 처리의 어려움**: 특히 비동기 작업을 다룰 때, 상태 전이를 일관성 있게 관리하기가 어렵습니다.

### MVI의 장점
MVI는 이러한 문제를 해결하기 위해 다음과 같은 장점을 제공합니다:
- **단방향 데이터 흐름**: 상태 변화가 명확하게 Intent → State → UI로 흐르므로 디버깅이 용이합니다.
- **예측 가능한 상태 관리**: 모든 상태 변경이 명시적인 Intent에 의해 이루어지며, 중간 상태(PartialStateChange)를 통해 상태 변화의 세부 사항을 세밀하게 제어할 수 있습니다.
- **고립된 비즈니스 로직**: 상태 관리와 비즈니스 로직이 분리되므로 코드 재사용성과 테스트 가능성이 높아집니다.
- **의도 기반 상호작용**: 사용자의 액션(Intent)에 기반하여 UI를 갱신하므로, UI와 비즈니스 로직의 의도를 명확하게 구분할 수 있습니다.

## 프로젝트 구조 🗂️

이 프로젝트는 **Google Architecture** 원칙을 준수하며, `core`, `data`, `domain`, `presentation`의 네 가지 모듈로 구성됩니다.


### **모듈별 설명**

- **core**: 애플리케이션의 전역 설정 및 초기화를 담당합니다. `ComposeMVIApplication.kt` 파일은 Hilt를 사용한 의존성 주입을 설정합니다.
  
- **data**: 데이터 레이어로, API 호출, 로컬 DB 관리, 데이터 매핑 및 페이징 처리와 관련된 모든 작업을 처리합니다. Repository 패턴을 사용하여 Domain 레이어와 통신합니다.

- **domain**: 비즈니스 로직을 포함하며, Use Case를 통해 Presentation 레이어에 데이터를 제공합니다. 데이터 처리의 복잡성을 숨기고, 단순화된 인터페이스를 제공합니다.

- **presentation**: UI를 관리하며, Jetpack Compose를 사용하여 화면을 구성합니다. MVI 패턴을 구현하여 UI 상태와 사용자 이벤트 간의 상호작용을 관리합니다.

## CI 및 테스트 🧪

- **GitHub Actions**: PR 생성 시 자동 빌드 및 테스트를 수행하여 코드 품질을 유지합니다. 
- **Compose UI Test**: Jetpack Compose로 작성된 UI의 테스트를 지원합니다.
- **JUnit 및 Espresso**: 비즈니스 로직 및 UI 테스트를 위한 프레임워크로, 애플리케이션의 모든 기능을 검증합니다.
