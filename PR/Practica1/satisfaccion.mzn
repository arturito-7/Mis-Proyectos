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

% ----------------------------------------------------------------------------------------

% - Variables de salida ------------------------------------------------------------------

array[trabs, dias] of var 0..3: sol; % Dado un trabajador y un día, el turno que tiene asignado el trabajador ese dia (0 si libra)

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

constraint assert(MinDT > 0 -> forall(t1 in trabs) (count(t2 in trabs) (afines[t1, t2]) >= A),"Como MinDT es > 0 entonces cada trabajador tiene que tener al menos A trabajadores afines");

constraint assert(MinDT == 0 -> sum(t1 in trabs) (count(t2 in trabs) (afines[t1, t2]) >= A) >= sum(N),"Como MinDT es == 0 entonces tiene qe haber al menos A trabajadores afines entre sí para poder cubrir todos los turnos");

constraint assert(not exists(t in trabs) (afines[t,t]),"Un trabajador no puede ser afín a si mismo");

constraint assert(length(R) > 2, "Tiene que haber al menos 3 responsables (al menos uno por turno)");

% ----------------------------------------------------------------------------------------

% - Restricciones ------------------------------------------------------------------------

% 1. Cada turno tiene el número de trabajadores (“N1”, “N2” o “N3”) que le corresponde.
constraint forall(d in dias, i in turnos) (count(t in trabs) (sol[t, d] == i) == N[i]);

% 2. Un trabajador solo puede estar en un turno cada día.

% Se cumple por la propia estructura de sol, que guarda para cada día en qué turno esta asignado el trabajador, por lo que es imposible que un trabajador este en dos turnos a la vez.

% 3. Dado un número “MaxDT”, garantizar que nadie trabaja “MaxDT” días consecutivos.
constraint forall(t in trabs, d in 1..D-MaxDT) (exists(i in 0..MaxDT) (sol[t, d + i] == 0));

% 4. Dado un número “MaxDL”, garantizar que nadie tiene “MaxDL” días libres consecutivos.
constraint forall(t in trabs, d in 1..D-MaxDL) (exists(i in 0..MaxDL) (sol[t, d + i] != 0));

% 5. Dado un número “MinDT”, garantizar que todos trabajan como mínimo “MinDT” en los “D” días.
constraint forall(t in trabs) (count(d in dias) (sol[t, d] != 0) >= MinDT);

% 6. Si un trabajador hace el último turno de un día entonces no puede tener el primero del día siguiente.
constraint forall(t in trabs, d in 1..D-1) (sol[t, d] == 3 -> sol[t, d + 1] != 1);

% 7. Si un trabajador hace el último turno dos días seguidos entonces tiene que librar el día siguiente.
constraint forall(t in trabs, d in 1..D-2) ((sol[t, d] == 3 /\ sol[t, d + 1] == 3) -> sol[t, d + 2] == 0);
%constraint not exists(t in trabs, d in 1..D-2) (sol[t, d] == 3 /\ sol[t, d + 1] == 3 /\ sol[t, d + 2] != 0);

% 8. Dada una serie de parejas de trabajadores afines, que se indicarán con una matriz 1..T x 1..T de Booleanos “afines”, y un número “A”, cada trabajador de un turno tiene que estar con al menos A trabajadores afines en ese turno.
constraint forall(d in dias, t1 in trabs) (count(t2 in trabs) (sol[t1, d] == sol[t2, d] /\ afines[t1, t2]) >= A * (sol[t1, d] != 0)); % no hace falta t1 != t2 ya que si es el mismo trabajador, no es afin a si mismo

% 9. Sea “R” el conjunto de trabajadores (que se obtendrá como un set of números de trabajador) que tienen la categoría de encargados. En cada turno debe haber al menos un responsable.
%constraint forall(d in dias, i in turnos) (exists(t in R) (sol[t, d] == i));
constraint forall(d in dias, i in turnos) (count(t in R) (sol[t, d] == i) >= 1);

% ----------------------------------------------------------------------------------------

solve satisfy;

output [
  "    " ++ concat([ "D" ++ show(d) ++ " " | d in 1..D ]) ++ "\n" ++  % Encabezado de días
  concat([
    if t in R then "R" else "T" endif ++ show(t) ++ " | " ++ concat([ show(sol[t, d]) ++ "  " | d in 1..D ]) ++ "\n"
    | t in 1..T
  ]) ++ "\nN1 = \(N1), N2 = \(N2), N3 = \(N3), \nMaxDT = \(MaxDT),\nMaxDL = \(MaxDL),\nMinDT = \(MinDT), \nA = \(A)"
];