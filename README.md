# ms-campuslab-notify

Microservicio de notificaciones de CampusLab.

## Tecnologías

- Java
- Spring Boot
- RabbitMQ

## Base de datos

No utiliza base de datos.

## Procesamiento actual

Notify consume y valida comandos de notificación, pero sus acciones finales
están simuladas: registra `eventId` y `type` en logs. No envía correos, no crea
tickets externos ni genera vouchers.

## Exposición

No expone endpoints HTTP públicos.

## Colas

Notify consume estas colas, creadas y administradas por
`ms-campuslab-rabbitmq`:

```text
q.cmd.email    -> processEmail
q.cmd.prep     -> processPreparation
q.cmd.voucher  -> processVoucher
```

Notify no declara colas, exchanges, bindings ni dead-letter queues.
Después de un procesamiento exitoso, el consumidor confirma el mensaje. Los
fallos recuperables se reintentan una vez; un mensaje inválido o un segundo
fallo se rechaza sin requeue y RabbitMQ lo enruta a la DLQ configurada para la
cola de origen.

## Formato de mensaje

```json
{
  "type": "BOOKING_REQUESTED",
  "eventId": "event-id",
  "timestamp": "2026-10-18T15:00:00Z",
  "traceId": "trace-id",
  "correlationId": "booking-id",
  "payload": {}
}
```

El contrato AsyncAPI está en [`asyncapi.yaml`](./asyncapi.yaml). El payload
puede incluir datos de reserva; el servicio no lo escribe en los logs.

## Variables de entorno

```env
RABBITMQ_HOST=
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```
