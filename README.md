# ms-campuslab-notify

Microservicio de notificaciones de CampusLab.

## Tecnologías

- Java
- Spring Boot
- RabbitMQ

## Base de datos

No utiliza base de datos.

## Funcionalidades

- Procesar notificaciones para estudiantes.
- Procesar tickets de preparación para técnicos.
- Procesar solicitudes de generación de vouchers.
- Consumir mensajes de RabbitMQ.

## Exposición

No expone endpoints HTTP públicos.

## Colas

```text
q.cmd.email
q.cmd.prep
q.cmd.voucher
```

## Dead Letter Queues

```text
q.cmd.email.dlq
q.cmd.prep.dlq
q.cmd.voucher.dlq
```

## Exchanges

```text
cmd.direct
cmd.topic
cmd.dead.dlx
```

## Formato de mensaje

```json
{
  "type": "",
  "eventId": "",
  "timestamp": "",
  "traceId": "",
  "correlationId": "",
  "payload": {}
}
```

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
