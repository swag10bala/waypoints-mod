# Waypoints 1.8.9

`Waypoints` e um mod client-side para Forge 1.8.9 focado em uma coisa simples: marcar coordenadas importantes sem encher o jogo de interface desnecessaria.

Ele foi organizado a partir de uma source decompilada e ajustado para virar um projeto usavel de novo. Hoje o repositorio ja tem a source limpa, a release pronta e as mudancas que foram feitas para o comportamento ficar melhor no uso real.

## O que o mod faz

- cria waypoints com nome, grupo, coordenadas, dimensao e cor
- mostra label no mundo com nome e/ou distancia
- permite editar, esconder, mostrar e deletar waypoints
- separa os waypoints por servidor ou mundo singleplayer
- permite copiar e compartilhar waypoint pelo chat
- mostra confirmacao antes de aceitar waypoint compartilhado
- tem ajustes visuais para escala, padding e opacidade das labels e do menu

## O que mudou nesta build

Essa versao nao e uma copia crua da source decompilada. Algumas partes foram ajustadas:

- a opcao `Show Beam` foi removida do mod inteiro
- o waypoint ficou preso no lugar certo de forma mais estavel quando voce chega perto da label
- a renderizacao passou a respeitar a camera real, entao funciona melhor com `F5` e tambem com `freelook`
- waypoint compartilhado nao usa mais rainbow
- compartilhamento agora aparece em laranja
- o formato de share foi trocado para `[wp;...]`, o que ajuda a evitar filtro de link em alguns servidores
- o parser ainda aceita o formato antigo para manter compatibilidade

## Como usar

Depois de instalar o jar no Forge 1.8.9, o mod adiciona dois atalhos na categoria `Waypoints` do menu de controles:

- criar waypoint
- abrir a lista de waypoints

Na tela de criacao/edicao voce pode definir:

- nome
- grupo
- `x`, `y`, `z`
- dimensao
- cor
- `Show Text`
- `Show Distance`
- `Direction Only`

Na lista de waypoints voce consegue:

- adicionar novo waypoint
- editar waypoint existente
- esconder ou mostrar individualmente
- esconder ou mostrar todos
- apagar waypoint
- ajustar a aparencia das labels e do menu

## Compartilhamento

Quando voce copia um waypoint, o mod gera uma mensagem no formato seguro:

```text
(Player) Nome do waypoint, [wp;x;y;z;dim;color;flags;contexto]
```

Quando essa mensagem aparece no chat e bate com o contexto atual, o mod troca o texto por um aviso clicavel em laranja. Ao clicar, abre uma confirmacao para criar o waypoint compartilhado.

Internamente o comando usado para abrir essa confirmacao e:

```text
/waypointshared <id>
```

Esse comando e interno do fluxo de chat. Nao e uma feature pensada para ficar digitando manualmente.

## Onde os dados ficam salvos

Os arquivos ficam em:

```text
.minecraft/config/waypoints/
```

Ali voce vai encontrar:

- `settings.json` para configuracoes visuais
- `server_<ip>.json` para waypoints de cada servidor
- `world_<nome>.json` para singleplayer
- `global.json` como fallback quando nao existe contexto melhor

Isso evita misturar waypoint de um servidor com outro.

## Instalar

1. Tenha o Forge `1.8.9` instalado.
2. Baixe a release do mod na pagina de releases do repositorio.
3. Coloque o jar em `.minecraft/mods`.
4. Deixe apenas um jar do `Waypoints` ativo na pasta para evitar conflito de mod duplicado.

Release atual:

- `v1.0.0`
- `waypoints-1.0.0.jar`

## Compilar a source

O projeto builda com Gradle e foi validado com JDK 17 para o ambiente de build.

```powershell
.\gradlew.bat build
```

Saida esperada:

```text
build/libs/waypoints-1.0.0.jar
```

## Estrutura do projeto

- `src/main/java/com/waypoints` - codigo do mod
- `src/main/resources` - metadata e resources

O repositorio do GitHub foi mantido enxuto de proposito. A ideia ali e hospedar a source do mod e as releases, sem o entulho do template original.

## Repositorio e release

- repositorio: `https://github.com/swag10bala/waypoints-mod`
- release atual: `https://github.com/swag10bala/waypoints-mod/releases/tag/v1.0.0`

Se a ideia for evoluir o mod daqui pra frente, o ponto natural e continuar em cima de `com.waypoints`, porque o que era resto do template ja foi removido.
