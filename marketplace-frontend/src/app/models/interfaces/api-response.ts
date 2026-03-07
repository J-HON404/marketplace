/**
 * ApiResponse<T> definisce il formato standard della risposta HTTP dal backend.
 * Proprietà:
 * - message: stringa opzionale che può contenere un messaggio di conferma, errore o informativo.
 * - data: payload generico di tipo T che contiene i dati effettivi della risposta.
 */

export interface ApiResponse<T> {
  message: string | null;
  data: T;
}