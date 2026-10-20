# CineFavoritos — Kotlin + MVVM + Coil

Aplicativo Android para visualizar e editar uma lista de filmes favoritos. Desenvolvido em Kotlin com View Binding, arquitetura MVVM, RecyclerView e Coil 2 para carregar pôsteres pela URL.

## Recursos
- Lista inicial de filmes com título, nota e pôster.
- Edição de título, nota e URL da imagem.
- Adição e remoção de filmes.
- Validação de título e nota (0 a 10).
- Interface atualizada a partir do StateFlow do ViewModel.
- Carregamento de imagens com Coil e placeholder de erro.

## Como executar
1. Abra a pasta do projeto no Android Studio (Giraffe ou mais recente).
2. Aguarde a sincronização do Gradle.
3. Execute no emulador ou dispositivo Android (minSdk 24).

Os dados ficam em memória durante a execução; persistência local não faz parte do escopo solicitado.
