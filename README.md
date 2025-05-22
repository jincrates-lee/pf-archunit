# Order Management API

주문 관리 시스템의 REST API 문서입니다.

## 📋 목차

- [개요](#개요)
- [API 엔드포인트](#api-엔드포인트)
- [데이터 모델](#데이터-모델)
- [상태 코드](#상태-코드)
- [예외 처리](#예외-처리)
- [사용 예시](#사용-예시)

## 개요

이 API는 주문의 생성, 조회, 수정, 삭제 및 상태 관리 기능을 제공합니다. RESTful 설계 원칙을 따라 구현되었으며, JSON 형태로 데이터를 주고받습니다.

**Base URL**: `/api/orders`

## API 엔드포인트

### 1. 주문 생성

새로운 주문을 생성합니다.

```http
POST /api/orders
```

**요청 본문:**
```json
{
  "userId": "user123",
  "quantity": 2,
  "description": "상품 주문"
}
```

**응답:**
- **상태 코드**: `201 Created`
- **응답 본문**: [OrderResponse](#orderresponse)

---

### 2. 전체 주문 조회

모든 주문 목록을 조회합니다.

```http
GET /api/orders
```

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: `Array<`[OrderResponse](#orderresponse)`>`

---

### 3. 특정 주문 조회

주문 코드로 특정 주문을 조회합니다.

```http
GET /api/orders/{orderCode}
```

**경로 변수:**
- `orderCode` (string): 주문 코드

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: [OrderResponse](#orderresponse)

---

### 4. 주문 수정

기존 주문 정보를 수정합니다.

```http
PUT /api/orders/{orderCode}
```

**경로 변수:**
- `orderCode` (string): 주문 코드

**요청 본문:**
```json
{
  "userId": "user123",
  "quantity": 3,
  "description": "수정된 주문 설명"
}
```

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: [OrderResponse](#orderresponse)

---

### 5. 주문 삭제

주문을 삭제합니다. (취소 가능한 상태의 주문만 삭제 가능)

```http
DELETE /api/orders/{orderCode}
```

**경로 변수:**
- `orderCode` (string): 주문 코드

**응답:**
- **상태 코드**: `204 No Content`

---

### 6. 주문 취소

주문 상태를 취소로 변경합니다.

```http
PUT /api/orders/{orderCode}/cancel
```

**경로 변수:**
- `orderCode` (string): 주문 코드

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: [OrderResponse](#orderresponse)

**주의사항:**
- `REQUESTED`, `CONFIRMED`, `PROCESSING` 상태에서만 취소 가능

---

### 7. 주문 확정

주문 상태를 확정으로 변경합니다.

```http
PUT /api/orders/{orderCode}/confirm
```

**경로 변수:**
- `orderCode` (string): 주문 코드

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: [OrderResponse](#orderresponse)

**주의사항:**
- `REQUESTED` 상태에서만 확정 가능

---

### 8. 사용자별 주문 조회

특정 사용자의 주문 목록을 조회합니다.

```http
GET /api/orders/user/{userId}
```

**경로 변수:**
- `userId` (string): 사용자 ID

**쿼리 파라미터:**
- `status` (string, optional): 주문 상태 필터

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: `Array<`[OrderResponse](#orderresponse)`>`

**예시:**
```http
GET /api/orders/user/user123?status=CONFIRMED
```

---

### 9. 주문 검색

다양한 조건으로 주문을 검색합니다.

```http
GET /api/orders/search
```

**쿼리 파라미터:**
- `status` (string, optional): 주문 상태
- `userId` (string, optional): 사용자 ID
- `startDate` (string, optional): 시작 날짜 (YYYY-MM-DD)
- `endDate` (string, optional): 종료 날짜 (YYYY-MM-DD)

**응답:**
- **상태 코드**: `200 OK`
- **응답 본문**: `Array<`[OrderResponse](#orderresponse)`>`

**예시:**
```http
GET /api/orders/search?status=COMPLETED&startDate=2024-01-01&endDate=2024-12-31
```

## 데이터 모델

### CreateOrderCommand

주문 생성 요청 데이터

```json
{
  "userId": "string (필수)",
  "quantity": "integer (필수, 양수)",
  "description": "string (선택)"
}
```

### UpdateOrderCommand

주문 수정 요청 데이터

```json
{
  "userId": "string (필수)",
  "quantity": "integer (필수)",
  "description": "string (선택)"
}
```

### OrderResponse

주문 응답 데이터

```json
{
  "code": "string",
  "userId": "string",
  "quantity": "integer",
  "description": "string",
  "status": "OrderStatus",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### OrderStatus

주문 상태 열거형

| 상태 | 설명 | 다음 가능한 상태 |
|------|------|------------------|
| `REQUESTED` | 주문 접수 | `CONFIRMED`, `CANCELLED` |
| `CONFIRMED` | 주문 확정 | `PROCESSING`, `CANCELLED` |
| `PROCESSING` | 처리 중 | `SHIPPED`, `CANCELLED` |
| `SHIPPED` | 배송 중 | `DELIVERED` |
| `DELIVERED` | 배송 완료 | `COMPLETED`, `REFUNDED` |
| `COMPLETED` | 주문 완료 | - |
| `CANCELLED` | 주문 취소 | - |
| `REFUNDED` | 환불 완료 | - |

## 상태 코드

| 상태 코드 | 설명 |
|-----------|------|
| `200 OK` | 요청 성공 |
| `201 Created` | 리소스 생성 성공 |
| `204 No Content` | 요청 성공, 반환할 데이터 없음 |
| `400 Bad Request` | 잘못된 요청 |
| `404 Not Found` | 리소스를 찾을 수 없음 |
| `409 Conflict` | 상태 충돌 (잘못된 상태 전환) |
| `500 Internal Server Error` | 서버 내부 오류 |

## 예외 처리

### OrderNotFoundException
주문을 찾을 수 없을 때 발생합니다.

**응답 예시:**
```json
{
  "error": "주문을 찾을 수 없습니다: ORDER_12345"
}
```

### InvalidOrderStateException
잘못된 상태 전환을 시도할 때 발생합니다.

**응답 예시:**
```json
{
  "error": "현재 상태(주문 완료)에서는 주문을 취소할 수 없습니다."
}
```

### 유효성 검증 오류
요청 데이터가 유효하지 않을 때 발생합니다.

**응답 예시:**
```json
{
  "error": "수량은 양수여야 합니다."
}
```

## 사용 예시

### 1. 주문 생성부터 완료까지의 플로우

```bash
# 1. 주문 생성
curl -X POST /api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "quantity": 2,
    "description": "노트북 주문"
  }'

# 응답: ORDER_ABC123 생성됨

# 2. 주문 확정
curl -X PUT /api/orders/ORDER_ABC123/confirm

# 3. 주문 조회
curl -X GET /api/orders/ORDER_ABC123
```

### 2. 사용자별 주문 검색

```bash
# 특정 사용자의 확정된 주문만 조회
curl -X GET "/api/orders/user/user123?status=CONFIRMED"
```

### 3. 기간별 주문 검색

```bash
# 2024년 1월의 완료된 주문 검색
curl -X GET "/api/orders/search?status=COMPLETED&startDate=2024-01-01&endDate=2024-01-31"
```

### 4. 주문 취소

```bash
# 주문 취소 (REQUESTED, CONFIRMED, PROCESSING 상태에서만 가능)
curl -X PUT /api/orders/ORDER_ABC123/cancel
```

## 참고사항

- 모든 날짜/시간 데이터는 ISO 8601 형식을 따릅니다.
- 주문 코드는 시스템에서 자동 생성되며, `ORDER_` 접두사를 가집니다.
- 상태 전환은 비즈니스 규칙에 따라 제한됩니다.
- API는 JSON 형태의 요청/응답만 지원합니다.

## 아키텍처 정보

이 프로젝트는 Clean Architecture 원칙을 따라 구현되었습니다.

**아키텍처 검증**: [ArchUnit 가이드](https://www.notion.so/petfriends/ArchUnit-14b6fc4eb78a80648ad5c87bd4c48725?pvs=4)
