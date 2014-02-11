REM @ECHO OFF
ECHO # EXECUTAR AS PERMISSOES DE ACESSO.
java -jar -Djava.security.policy=rmi.policy RmiChat.jar
PAUSE