package serie2.problema

import serie2.part4.HashMap
object PointProcessorCustom {
    // Estrutura que representa um ponto no plano
    private data class Coordinate(val x: Float, val y: Float)

    // Mapa personalizado para associar pontos à sua presença nos ficheiros
    private var pointTable = HashMap<Coordinate, Array<Boolean>>()

    // Função que carrega os pontos dos ficheiros para a estrutura de dados
    fun load(first: String, second: String) {
        val rdr1 = createReader(first)
        val rdr2 = createReader(second)

        var entry1 = rdr1.readLine()
        var entry2 = rdr2.readLine()

        // Ignora comentários e cabeçalhos até encontrar pontos
        while (entry1.split(' ')[0] != "v" && entry2.split(' ')[0] != "v") {
            entry1 = rdr1.readLine()
            entry2 = rdr2.readLine()
        }

        // Lê as linhas até ao fim de ambos os ficheiros
        while (entry1 != null || entry2 != null) {
            if (entry1 != null) {
                val tokens = entry1.split(' ')
                val point = Coordinate(tokens[2].toFloat(), tokens[3].toFloat())
                val prev = pointTable[point]

                // Marca presença do ponto no primeiro ficheiro
                if (prev != null && !prev[0])
                    pointTable.put(point, arrayOf(true, true))
                else if (prev == null)
                    pointTable.put(point, arrayOf(true, false))

                entry1 = rdr1.readLine()
            }

            if (entry2 != null) {
                val tokens = entry2.split(' ')
                val point = Coordinate(tokens[2].toFloat(), tokens[3].toFloat())
                val prev = pointTable.get(point)

                // Marca presença do ponto no segundo ficheiro
                if (prev != null && !prev[1])
                    pointTable.put(point, arrayOf(true, true))
                else if (prev == null)
                    pointTable.put(point, arrayOf(false, true))

                entry2 = rdr2.readLine()
            }
        }

        // Fecha os leitores dos ficheiros
        rdr1.close()
        rdr2.close()
    }

    // Escreve todos os pontos num ficheiro (união)
    fun union(output: String) {
        val writer = createWriter(output)
        for (entry in pointTable) {
            writer.println("${entry.key.x} , ${entry.key.y}")
        }
        writer.close()
    }

    // Escreve apenas os pontos comuns a ambos os ficheiros
    fun intersection(output: String) {
        val writer = createWriter(output)
        for (entry in pointTable) {
            if (entry.value[0] && entry.value[1])
                writer.println("${entry.key.x} , ${entry.key.y}")
        }
        writer.close()
    }

    // Escreve os pontos exclusivos a um dos ficheiros (diferença simétrica)
    fun difference(output: String) {
        val writer = createWriter(output)
        for (entry in pointTable) {
            if (entry.value[0] xor entry.value[1])
                writer.println("${entry.key.x} , ${entry.key.y}")
        }
        writer.close()
    }
}

fun main() {
    // Testa as funções com os ficheiros fornecidos
    PointProcessorCustom.load("Test1.co", "Test2.co")
    PointProcessorCustom.union("union.co")
    PointProcessorCustom.difference("difference.co")
    PointProcessorCustom.intersection("intersection.co")
}
