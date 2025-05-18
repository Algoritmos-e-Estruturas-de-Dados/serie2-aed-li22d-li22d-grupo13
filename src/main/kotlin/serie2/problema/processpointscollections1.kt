package serie2.problema

object PointProcessor {
    // Define um ponto no plano com coordenadas x e y
    private data class Coordinate(val x: Float, val y: Float)

    // Mapa para armazenar os pontos e a sua origem nos ficheiros (array de dois booleanos)
    private var pointMap = HashMap<Coordinate, Array<Boolean>>()

    // Carrega os dados de dois ficheiros .co para a estrutura de dados
    fun load(input1: String, input2: String) {
        val fileReader1 = createReader(input1)
        val fileReader2 = createReader(input2)

        var line1 = fileReader1.readLine()
        var line2 = fileReader2.readLine()

        // Avança até encontrar a primeira linha válida com ponto ('v') em ambos os ficheiros
        while (line1.split(' ')[0] != "v" && line2.split(' ')[0] != "v") {
            line1 = fileReader1.readLine()
            line2 = fileReader2.readLine()
        }

        // Continua a ler até que ambas as linhas sejam nulas (fim dos ficheiros)
        while (line1 != null || line2 != null) {
            // Processa a linha do primeiro ficheiro
            line1?.let {
                val values = it.split(' ')
                val coord = Coordinate(values[2].toFloat(), values[3].toFloat())
                val current = pointMap[coord]

                // Atualiza a presença do ponto no primeiro ficheiro
                if (current != null && !current[0])
                    pointMap[coord] = arrayOf(true, true)
                else if (current == null)
                    pointMap[coord] = arrayOf(true, false)

                line1 = fileReader1.readLine()
            }

            // Processa a linha do segundo ficheiro
            line2?.let {
                val values = it.split(' ')
                val coord = Coordinate(values[2].toFloat(), values[3].toFloat())
                val current = pointMap[coord]

                // Atualiza a presença do ponto no segundo ficheiro
                if (current != null && !current[1])
                    pointMap[coord] = arrayOf(true, true)
                else if (current == null)
                    pointMap[coord] = arrayOf(false, true)

                line2 = fileReader2.readLine()
            }
        }

        // Fecha os leitores
        fileReader1.close()
        fileReader2.close()
    }

    // Escreve todos os pontos (união) num ficheiro de saída
    fun union(output: String) {
        val writer = createWriter(output)
        for ((coord, _) in pointMap) {
            writer.println("${coord.x} , ${coord.y}")
        }
        writer.close()
    }

    // Escreve apenas os pontos que existem em ambos os ficheiros (interseção)
    fun intersection(output: String) {
        val writer = createWriter(output)
        for ((coord, flags) in pointMap) {
            if (flags[0] && flags[1])
                writer.println("${coord.x} , ${coord.y}")
        }
        writer.close()
    }

    // Escreve apenas os pontos exclusivos de um dos ficheiros (diferença simétrica)
    fun difference(output: String) {
        val writer = createWriter(output)
        for ((coord, flags) in pointMap) {
            if (flags[0] xor flags[1])
                writer.println("${coord.x} , ${coord.y}")
        }
        writer.close()
    }
}

fun main() {
    // Executa as operações sobre dois ficheiros de teste
    PointProcessor.load("Test1.co", "Test2.co")
    PointProcessor.union("union.co")
    PointProcessor.difference("difference.co")
    PointProcessor.intersection("intersection.co")
}
