% - Variables de entrada -----------------------------------------------------------------

int: D; % Número de días

set of int: dias = 1..D; % Conjunto de los dias

int: T; % Número de trabajadores

set of int: trabs = 1..T; % Conjunto de los trabajadores

int: N1; % Número de trabajadores necesarios para el turno de mañana
int: N2; % Número de trabajadores necesarios para el turno de tarde
int: N3; % Número de trabajadores necesarios para el turno de noche

set of int: turnos = 1..3; % Conjunto de los turnos

array[turnos] of int: N = [N1, N2, N3]; % Número de trabajadores necesarios para cada turno

int: MaxDT; % Máximo número de días consecutivos que uno puede trabajar
int: MaxDL; % Máximo número de días consecutivos que uno puede librar
int: MinDT; % Mínimo número de días que uno debe trabajar en "D" días

array[trabs, trabs] of bool: afines; % Matriz de afinidad

int: A; % Mínimo número de trabajadores afines con los que todos los trabajadores deben trabajar

set of trabs: R; % Conjunto de trabajadores encargados

set of trabs: anti; % Conjunto de trabajadores que pueden trabajar en turnos anti estrés

set of trabs: doblar; % Conjunto de trabajadores que solicitan doblar turnos

array[trabs] of set of dias: diasNT; % Para cada trabajador, el conjunto de dias que no quieren trabajar

array[trabs] of set of turnos: turnosNT; % Para cada trabajador, el conjunto de turnos que no quieren trabajar

array[dias, trabs] of set of turnos: turnosDiaNT; % Para cada trabajador, el conjunto de turnos para cada dia que no quieren trabajar

% ----------------------------------------------------------------------------------------

% - Variables de salida ------------------------------------------------------------------

array[trabs, dias, 0..3] of var 0..1: sol; % Dado un trabajador, un día y un turno, 1 si está en el turno, 0 si no 

% NOTA: si un trabajador tiene en un dia todo a 0 es que libre pero no cuenta, si tiene todo a 0 y sol[t, d, 0] == 1 es que libre pero cuenta

% ----------------------------------------------------------------------------------------

% - Asserts ------------------------------------------------------------------------------

constraint assert(D > 0, "Tiene que haber al menos un día para que se pueda planificar");

%constraint assert(T >= N1 + N2 + N3, "Tiene que haber al menos tantos trabajadores como trabajadores sean necesarios para cubrir los 3 turnos de un día");
constraint assert(T >= sum(N), "Tiene que haber al menos tantos trabajadores como trabajadores sean necesarios para cubrir los 3 turnos de un día");

%constraint assert(if T == N1 + N2 + N3 then MaxDT < D endif, "Si todos tienen que trabajar todos los días y el MaxDt es menor que D, no se puede"); % CASO PUNTUAL

% constraint assert(N1 > 0, "Tiene que haber al menos un trabajador para el turno de mañana (N1)");
% constraint assert(N2 > 0, "Tiene que haber al menos un trabajador para el turno de tarde (N2)");
% constraint assert(N3 > 0, "Tiene que haber al menos un trabajador para el turno de noche (N3)");
constraint assert(min(N) > 0, "Tiene que haber al menos un trabajador por turno");

constraint assert(MaxDT > 0, "El máximo de dias consecutivos que se puede trabajar tiene que ser mínimo un día, sino no se podría trabajar");

constraint assert(MaxDL >= 0, "El máximo de dias consecutivos que se puede librar tiene que ser >= 0");

%constraint assert(0 <= MinDT /\ MinDT <= D, "El mínimo número de días que se debe trabajar tiene que estar entre 0 y el número de días (D) incluidos");
constraint assert(MinDT in 0..D, "El mínimo número de días que se debe trabajar tiene que estar entre 0 y el número de días (D) incluidos");
 
/*
D div MaxDT+1 te da los paquetes de días en los que como mínimo tengo que librar un día para que se cumpla el MaxDT
por lo que si se lo restamos a los dias totales, nos sale el máximo de días que podemos trabajar
*/ 
constraint assert(MinDT <= D - (D div (MaxDT + 1)), "MinDT no compatible con D y MaxDT");
 
%constraint assert(0 <= A /\ A < min({N1, N2, N3}), "tiene que haber entre 0 (incluido) y el número de trabajadores del turno (excluido) trabajadores afines en el turno");
constraint assert(A in 0..min(N)-1, "Tiene que haber entre 0 (incluido) y el número de trabajadores del turno (excluido) trabajadores afines en el turno");

constraint assert(MinDT > 0 -> forall(t1 in trabs) (count(t2 in trabs)(afines[t1, t2]) >= A ),"Cada trabajador tiene que tener al menos A trabajadores afines");

constraint assert(MinDT == 0 -> sum(t1 in trabs) (count(t2 in trabs) (afines[t1, t2]) >= A) >= sum(N),"Como MinDT es == 0 entonces tiene qe haber al menos A trabajadores afines entre sí para poder cubrir todos los turnos");

constraint assert(not exists(t in trabs) (afines[t,t]),"Un trabajador no puede ser afín a si mismo");

constraint assert(length(R) > 2, "Tiene que haber al menos 3 responsables (al menos uno por turno)");

% ----------------------------------------------------------------------------------------

% - Restricciones básicas ----------------------------------------------------------------

% 1. Cada turno tiene el número de trabajadores (“N1”, “N2” o “N3”) que le corresponde.
constraint forall(d in dias, i in turnos) (sum(t in trabs) (sol[t, d, i]) == N[i]);

% 2. Un trabajador solo puede estar en un turno cada día.

% Los trabajadores que no doblan solo pueden tener un turno como máximo
constraint forall(t in trabs diff doblar, d in dias) (sum(i in 0..3) (sol[t, d, i]) <= 1);

% Los trabajadores que doblan solo pueden tener 2 turnos como máximo
constraint forall(t in doblar, d in dias) (sum(i in 0..3) (sol[t, d, i]) <= 2);

% Un trabajador no tiene ningún turno si ha doblado el dia anterior o ha solicitado el dia libre
constraint forall(t in trabs, d in dias) (sum(i in 0..3) (sol[t,d,i]) == 0 -> d in diasNT[t] \/ (d > 1 /\ t in doblar /\ sum(i in turnos) (sol[t, d - 1, i]) == 2));

% 3. Dado un número “MaxDT”, garantizar que nadie trabaja “MaxDT” días consecutivos.
constraint forall(t in trabs, d in 1..D-MaxDT) (exists(j in 0..MaxDT) (sum(i in turnos) (sol[t, d + j, i]) == 0)); % no trabaja en ningún turno ese día, no sirve sol[t, d, 0] == 1 ya que puede ser sol[t, d, 0] == 0 y no trabajar en ningún turno por las restricciónes adicionales

% 4. Dado un número “MaxDL”, garantizar que nadie tiene “MaxDL” días libres consecutivos.
constraint forall(t in trabs, d in 1..D-MaxDL) (exists(i in 0..MaxDL) (sol[t, d + i, 0] == 0)); % no libra ese día

% 5. Dado un número “MinDT”, garantizar que todos trabajan como mínimo “MinDT” en los “D” días.
constraint forall(t in trabs) (sum(d in dias) (sol[t, d, 0] == 0) >= MinDT); % Si no libra, es que trabaja

% 6. Si un trabajador hace el último turno de un día entonces no puede tener el primero del día siguiente.
constraint forall(t in trabs, d in 1..D-1) (sol[t, d, 3] == 1 -> sol[t, d + 1, 1] == 0);

% 7. Si un trabajador hace el último turno dos días seguidos entonces tiene que librar el día siguiente.
constraint forall(t in trabs, d in 1..D-2) ((sol[t, d, 3] == 1 /\ sol[t, d + 1, 3] == 1) -> sol[t, d + 2, 0] == 1);

% 8. Dada una serie de parejas de trabajadores afines, que se indicarán con una matriz 1..T x 1..T de Booleanos “afines”, y un número “A”, cada trabajador de un turno tiene que estar con al menos A trabajadores afines en ese turno.
constraint forall(t1 in trabs, d in dias, i in turnos) (count(t2 in trabs)  (sol[t1, d, i] == 1 /\ sol[t2, d, i] == 1 /\ afines[t1, t2]) >= A * sol[t1, d, i]); % no hace falta t1 != t2 ya que si es el mismo trabajador, no es afin a si mismo

% 9. Sea “R” el conjunto de trabajadores (que se obtendrá como un set of números de trabajador) que tienen la categoría de encargados. En cada turno debe haber al menos un responsable.
constraint forall(d in dias, i in turnos) (sum(t in R) (sol[t, d, i]) >= 1);

% ----------------------------------------------------------------------------------------

% - Restricciones adicionales ------------------------------------------------------------

% 1. Añadid la posibilidad de que algunos trabajadores puedan trabajar en “turnos anti estrés”, es decir, en turnos rotatorios. De esta manera si un trabajador trabaja dos mañanas, dos tardes y una noche consecutivas librará dos días seguidos.
constraint forall(t in anti, d in 1..D-6) ((sol[t, d, 1] == 1 /\ sol[t, d + 1, 1] == 1 /\ sol[t, d + 2, 2] == 1 /\ sol[t, d + 3, 2] == 1 /\ sol[t, d + 4, 3] == 1) -> (sol[t, d + 5, 0] == 1 /\ sol[t, d + 6, 0] == 1));

% 2. Añadid la posibilidad de que los trabajadores soliciten doblar turnos. De esta manera, un trabajador podrá trabajar durante dos turnos consecutivos en un mismo día, librando al día siguiente. Tened en cuenta que ese día libre no cuenta en la restricción de días consecutivos libres y hay que descontarlo de “MinDT”. Modificad las restricciones anteriores en case de ser necesario.

% Si tienen 2 turnos es porque son consecutivos
constraint forall(t in doblar, d in dias) (sum(i in 0..3) (sol[t, d, i]) == 2 -> ((sol[t, d, 1] == 1 /\ sol[t, d, 2] == 1) \/ (sol[t, d, 2] == 1 /\ sol[t, d, 3] == 1)));

% Si tiene 2 turnos, al dia siguiente libra
constraint forall(t in doblar, d in 1..D-1) (sum(i in 0..3) (sol[t, d, i]) == 2 -> sum(i in 0..3) (sol[t, d + 1, i]) == 0);

% ----------------------------------------------------------------------------------------

% - Optimización -------------------------------------------------------------------------

function var int: sumatorio(var int: n) = (n * (n + 1)) div 2;

% 3. Añadid la posibilidad de que los trabajadores pidan días que no quieren trabajar. Tened en cuenta que estos días no cuentan en la restricción de días consecutivos libres y que hay que descontarlos de “MinDT”. Convertid el problema en un problema de optimización, minimizando el número de veces que hacemos trabajar a alguien cuando no quiere. Pensad en formas de conseguir que los incumplimientos se distribuyan entre todos.

% Maximizar el número de dias libres concedidos 
%solve maximize sum(t in trabs, d in diasNT[t]) (sum(i in 0..3) (sol[t, d, i]) == 0);

% Es lo mismo que minimizar el número de dias libres no concedidos
%solve minimize sum(t in trabs) (count(d in diasNT[t]) (sum(i in 0..3) (sol[t, d, i]) > 0));

% Añadiendo el sumatorio, distribuimos mejor el incumplimiento, ya que cuantos más días se incumplan para un trabajador, más peso tiene cada día incumplido
%solve minimize sum(t in trabs) (sumatorio(count(d in diasNT[t]) (sum(i in 0..3) (sol[t, d, i]) > 0)));

% 4. Añadid la posibilidad de que los trabajadores pidan qué turno no quieren (único o para cada día), si es que tienen alguna preferencia. Convertid el problema en un problema de optimización, minimizando el número de veces que incumplimos la petición. Pensad en formas de conseguir que los incumplimientos se distribuyan entre todos

% Único
%solve minimize sum(t in trabs, d in dias, i in turnosNT[t]) (sol[t, d, i]);

%Distribuyendo los incumplimientos 
%solve minimize sum(t in trabs) (sumatorio(count(d in dias, i in turnosNT[t]) (sol[t, d, i] == 1)));

% Para cada dia
%solve minimize sum(t in trabs, d in dias, i in turnosDiaNT[d, t]) (sol[t, d, i]);

%Distribuyendo los incumplimientos 
%solve minimize sum(t in trabs) (sumatorio(count(d in dias, i in turnosDiaNT[d,t]) (sol[t, d, i] == 1)));

% ----------------------------------------------------------------------------------------

% - Extra --------------------------------------------------------------------------------

%Se minimiza el número de incumplimientos de lo solicitado dando un valor de 3 más a no trabajar un día que no se quiere que a trabajar en un día que no se quiere, repartiendolo de la mejor manera posible entre todos los trabajadores
%solve minimize sum(t in trabs) ((3 * sumatorio(count(d in dias, i in turnosNT[t]) (sol[t, d, i] == 1))) + sumatorio(count(d in diasNT[t]) (sum(i in 0..3) (sol[t, d, i]) > 0)));


%solve minimize sum(t in trabs) ((3 * sumatorio(count(d in dias, i in turnosDiaNT[d,t]) (sol[t, d, i] == 1))) + sumatorio(count(d in diasNT[t]) (sum(i in 0..3) (sol[t, d, i]) > 0)));


%Restricción todos los trabajadores trabajan por igual
solve minimize sum(t in trabs, i in turnos) (sumatorio(count(d in dias) (sol[t, d, i] == 1)));

% ----------------------------------------------------------------------------------------

output [
  "    " ++ concat([ "D" ++ show(d) ++ " " | d in 1..D ]) ++ "\n" ++  % Encabezado de días
  concat([
    if t in R then "R" else "T" endif ++ show(t) ++ " | " ++ concat([ show(sol[t, d, 0]) ++ show(sol[t, d, 1]) ++ show(sol[t, d, 2]) ++ show(sol[t, d, 3]) ++ "  " | d in 1..D ]) ++ "\n"
    | t in 1..T
  ]) ++ "\nN1 = \(N1), N2 = \(N2), N3 = \(N3), \nMaxDT = \(MaxDT),\nMaxDL = \(MaxDL),\nMinDT = \(MinDT), \nA = \(A)"
];
