package chetrari.vlad.rts.data

interface DataFetcher<Input, Output> {
    suspend fun fetch(input: Input): Output
}