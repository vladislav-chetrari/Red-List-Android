package chetrari.vlad.rts.data

interface DataMapper<Input, RawData, Output> {

    suspend fun map(input: Input, rawData: RawData): Output
}