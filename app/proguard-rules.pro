# Mantener todas las clases del conector MySQL
-keep class com.mysql.** { *; }

# Ignorar advertencias relacionadas con el paquete MySQL
-dontwarn com.mysql.**

# Mantener clases utilizadas por JDBC
-keep class java.sql.** { *; }
-dontwarn java.sql.**
