<template>
  <div class="collection">
    <div class="c-container">
      <div class="container-box">
        <div class="container-white">
          <div class="c-left-tab">
            <li class="menu-item menu-item-new " @click="dialogNewVisible=true">
              <div class="imgBox"></div>
              <span>新建收藏夹</span>
            </li>
            <el-dialog v-model="dialogNewVisible" title="新建收藏夹" width="400px" :center="true">
              <div style="text-align: center;margin: 0 auto">
                <div style="margin-bottom: 10px">
                  <el-input v-model="inputNewCollection" placeholder="收藏夹名称">
                    <template #append>
                      <el-button style="color: black" @click="createFavorite">新建</el-button>
                    </template>
                  </el-input>
                </div>
                <span class="dialog-footer">
            </span>
              </div>
            </el-dialog>
            <div v-for="(favorite,index) in favorites">
              <li class="menu-item" :key="index" :class="currentIndex === index ?'active-menu-item':''"
                  @click="clickFavorites(index)">
                <div class="menu-item-title">
                  <div class="imgBox"></div>
                  <span>{{ favorite.name}}</span>
                </div>
                <div class="menu-item-like">
              <span class="comment">
                <div class="imgBox"></div>
                 <span>{{ favorite.num }}</span>
              </span>
                </div>
              </li>
            </div>
          </div>

          <div class="c-right-content">
            <div class="list-wrap">
              <ul class="collection-list">
                <li>
                  <div class="titleHead">
                    <div style="height: 32px">
                      <span v-if="editCollectionName" class="collection-dir">
                        <span class="collection-folder-name">
                          {{this.favorites[this.currentIndex].name}}
                          <img @click="editCollectionName=false;this.folderName=this.favorites[this.currentIndex].name"
                               src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4xIiBpZD0iTGF5ZXJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHdpZHRoPSIzMnB4IiBoZWlnaHQ9IjMycHgiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgMzIgMzIiIHhtbDpzcGFjZT0icHJlc2VydmUiPiAgPGltYWdlIGlkPSJpbWFnZTAiIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgeD0iMCIgeT0iMCIKICAgIGhyZWY9ImRhdGE6aW1hZ2UvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQ0FBQUFBZ0NBUUFBQURaYzdKL0FBQUFCR2RCVFVFQUFMR1BDL3hoQlFBQUFDQmpTRkpOCkFBQjZKZ0FBZ0lRQUFQb0FBQUNBNkFBQWRUQUFBT3BnQUFBNm1BQUFGM0NjdWxFOEFBQUFBbUpMUjBRQS80ZVB6TDhBQUFBSGRFbE4KUlFma0J3SVdPeksxNW9iVkFBQUIxRWxFUVZSSXgrMlZQV2dVVVJTRnYvczJTMUFTTEF3WUNJWVVTV3VLa0ZKQ2lvQkZTaUdDQ0NhWgo5Mkl3WUNFaGRyWVdLUXlTd3RrWVFiUklSRVJVOGlPeUZoWnBncFVnZ2lBS29nZ3FGaXB1M0p2QzNablptZXpPTEdrc2ZOWGN1ZmQ4CmM4NTd3NHdvKzF0bW4vcC9BTkFTWHQ0OFdyb3FJOW9lM3BIZlhMR1hvK1ArUlpuVEQyYldleExNVkRmUjcyYWJqamhmdnRqRFliVjAKc253WGdCMXozTnVLUjFoSXlvRlAwY0s3SjlmLyt0YUZSQVFacVhoNVF6R01rTHNkbUovbnNTdmE2WUpSQzlxWGpLQUE4bG03WENscApwSEJOWi9qQnFDc2l2bzluWnJ6RlBVOUJYOVNWdzBFZStjT29jM0tpS3M5MGpCVTVJY0p1aE4xVVFFUU95QUhwcmUyYnB1VEtsQzAwCkFZakwxY1hsTlc5aW1seXN2WkdjTXRua2xHWFNDK1dTNmlBdU4rUGVyVXBuU085d2lHVjNvWUdESlZkai9nOW5xbkxRODNUUnhuUkQKQitWemtXS0gwMjQxZ210VmdId0R3SEk3L1VGUmtqRjdud1pyRDBCNUlBajJNemMyK1JDYUJSd0Q0TDJzNUJmUHZvMTNWVklCdkpKTAp1YWNUMjJUNllNY0EwdS9uM1NhYjljYlhXaG1zQi9oSUorZ1JYdnZQcGQ2ekRVTjBBdkExQVpCbmVncUFIbnJTdmN0V3lLeHV6cHg4Cnk1SVpRSDR4bXdDNGR5MEQ4aUFENUR2clp0QytER0QvLzQzc0FyNEhtc2REQ2x4TUFBQUFKWFJGV0hSa1lYUmxPbU55WldGMFpRQXkKTURJd0xUQTNMVEF5VkRFME9qVTVPalV3S3pBNE9qQXduS0xpT1FBQUFDVjBSVmgwWkdGMFpUcHRiMlJwWm5rQU1qQXlNQzB3TnkwdwpNbFF4TkRvMU9UbzFNQ3N3T0Rvd01PMy9Xb1VBQUFBZ2RFVllkSE52Wm5SM1lYSmxBR2gwZEhCek9pOHZhVzFoWjJWdFlXZHBZMnN1CmIzSm52TThkblFBQUFCaDBSVmgwVkdoMWJXSTZPa1J2WTNWdFpXNTBPanBRWVdkbGN3QXhwLys3THdBQUFCZDBSVmgwVkdoMWJXSTYKT2tsdFlXZGxPanBJWldsbmFIUUFNeklvOVBqMEFBQUFGblJGV0hSVWFIVnRZam82U1cxaFoyVTZPbGRwWkhSb0FETXkwRnM0ZVFBQQpBQmwwUlZoMFZHaDFiV0k2T2sxcGJXVjBlWEJsQUdsdFlXZGxMM0J1WnoreVZrNEFBQUFYZEVWWWRGUm9kVzFpT2pwTlZHbHRaUUF4Ck5Ua3pOamN6TVRrd3Mra0RqUUFBQUJGMFJWaDBWR2gxYldJNk9sTnBlbVVBTnpnd1FrTEpPUXI3QUFBQVJuUkZXSFJVYUhWdFlqbzYKVlZKSkFHWnBiR1U2THk4dllYQndMM1J0Y0M5cGJXRm5aV3hqTDJsdFozWnBaWGN5WHpsZk1UVTVNamczTnpVNE1qQXdOelkxTWpGZgpOelJmV3pCZGVvVnRGQUFBQUFCSlJVNUVya0pnZ2c9PSIgPjwvaW1hZ2U+Cjwvc3ZnPgo=">
                        </span>
                      </span>
                      <!---->
                      <div v-else class="input-edit-box">
                        <el-input class="collection-edit-name" v-model="folderName" placeholder="输入新名称"></el-input>
                        <img @click="editCollectionName=true"
                             src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4xIiBpZD0iTGF5ZXJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHdpZHRoPSI0OHB4IiBoZWlnaHQ9IjQ4cHgiIHZpZXdCb3g9IjAgMCA0OCA0OCIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgNDggNDgiIHhtbDpzcGFjZT0icHJlc2VydmUiPiAgPGltYWdlIGlkPSJpbWFnZTAiIHdpZHRoPSI0OCIgaGVpZ2h0PSI0OCIgeD0iMCIgeT0iMCIKICAgIGhyZWY9ImRhdGE6aW1hZ2UvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBREFBQUFBd0NBTUFBQUJnM0FtMUFBQUFCR2RCVFVFQUFMR1BDL3hoQlFBQUFDQmpTRkpOCkFBQjZKZ0FBZ0lRQUFQb0FBQUNBNkFBQWRUQUFBT3BnQUFBNm1BQUFGM0NjdWxFOEFBQUFybEJNVkVYLy8vLy9WVG5sUHpYa1BqVGoKUHpUalB6UGtQelRqUGpQd1N6emtRRFhqUHpQa1BqUG1UVFBrUGpUalBqVC9WVlhqUGpUalBqUHNRam5qUHpUalFUUGpRRFhqUHpUbQpRRFhqUHpUb1JrYmtQelBrUGpUbFFUVGtRRFBqUHpQalBqUGxQelBtUURQbFBqVGpQalRrUHpUbFFEVG1RRFBrUHpQalB6UGtRRFByClRqdmtQalBsUURUL1pqUGtQalBuUURqbVFqWGpQalBqUHpYbFB6UGpQalBuWFZQMXVyYnRpSUgvLy8veG9wMSt4VElqQUFBQU5IUlMKVGxNQUNVMlB0OXZzK1JGZ3IvVUtlL0VEWnVZYnlqZGM5MXZMQzMzeVQ1RGN1SkZRVHZheVl4VDBnWHdONm13RjBTQSsrbVZ0N09jUApUZ0FBQUFGaVMwZEVBSWdGSFVnQUFBQUhkRWxOUlFma0J3Y0NOem83ZWJwMUFBQUJpRWxFUVZSSXg1V1cyWnFDTUF5Rm83SUlvb0lMCk91cTRqZHZzVzBSOC94ZWJpbzYwdEtIbDNLWGZmeURwbGdJVVZhczNMTnR4WGNlMkd2VWFhTlQwL0JaeWF2bGVzd1FQMmgyVTFHa0gKQk40TkkxUXFDcnNxdnRkSFV2MmV6QThpTEZFMEtQTERHRXNWRDBWK2hGcU5oSHhpdlNIbXN1cEZlcDdWY2ErOE96YmhFY2Yvc3h1YQo4WWpobFg4d1NpaEw2cnJtazN6a21KeUswQ2s1NXNFazIyL1RmQ0JKMDdQSW45TTB5YVBwWlNkNi9PZlNnb1B4S2Y5VGp4bjhJbkFtClEwU2ZuUmRoLzR1SXhHT3JCck5Da1J3azg0Z3plRVRLb2VLeEFSWVNEaVdQRnRpb2RxaDV0TUZCdFVQTm93TXVVZzRWajI1MVErV1UKNWxXTFhsU2QxaVhCa3d1M29uaTFZdzNCRThVVG13ODJKSy9lM3JEbFlxTUR0TnZuc2RFUmhVTStvTHNFbnJOYjQrVVZEWFc3WnVETgoxSEM3eU9EOXc0eS9YNVh3YVpSVXhMV2hyMjg5SHd0TjZFZHZFQm9LYzJqK1VXeFpMS3ZTT3VTbXlDb3ZtU3RWMjJXeiswdjhoR2pzCmx6VS83R1djZmpwY3ROdHVoUE9oZVp4a0NsYkx4ZnoyL0ZuTHo1OC8rTTFxZGpURVVmRUFBQUFsZEVWWWRHUmhkR1U2WTNKbFlYUmwKQURJd01qQXRNRGN0TURaVU1UZzZOVFU2TlRnck1EZzZNREQ3K3Jtc0FBQUFKWFJGV0hSa1lYUmxPbTF2WkdsbWVRQXlNREl3TFRBMwpMVEEyVkRFNE9qVTFPalU0S3pBNE9qQXdpcWNCRUFBQUFDQjBSVmgwYzI5bWRIZGhjbVVBYUhSMGNITTZMeTlwYldGblpXMWhaMmxqCmF5NXZjbWU4engyZEFBQUFHSFJGV0hSVWFIVnRZam82Ukc5amRXMWxiblE2T2xCaFoyVnpBREduLzdzdkFBQUFGM1JGV0hSVWFIVnQKWWpvNlNXMWhaMlU2T2tobGFXZG9kQUEwT0lkZ2h5MEFBQUFXZEVWWWRGUm9kVzFpT2pwSmJXRm5aVG82VjJsa2RHZ0FORGgvejBlZwpBQUFBR1hSRldIUlVhSFZ0WWpvNlRXbHRaWFI1Y0dVQWFXMWhaMlV2Y0c1blA3SldUZ0FBQUJkMFJWaDBWR2gxYldJNk9rMVVhVzFsCkFERTFPVFF3TXpJNU5UaEJWb2s5QUFBQUVuUkZXSFJVYUhWdFlqbzZVMmw2WlFBeE1qUTNRa0loeHh2L0FBQUFSblJGV0hSVWFIVnQKWWpvNlZWSkpBR1pwYkdVNkx5OHZZWEJ3TDNSdGNDOXBiV0ZuWld4akwybHRaM1pwWlhjeVh6bGZNVFU1TWpVMU9EVXpNek13T0RVMgpPRE5mTkRaZld6QmQra0R5OEFBQUFBQkpSVTVFcmtKZ2dnPT0iID48L2ltYWdlPgo8L3N2Zz4K">
                        <img @click="updateFolderName"
                             src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4xIiBpZD0iTGF5ZXJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHdpZHRoPSI0OHB4IiBoZWlnaHQ9IjQ4cHgiIHZpZXdCb3g9IjAgMCA0OCA0OCIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgNDggNDgiIHhtbDpzcGFjZT0icHJlc2VydmUiPiAgPGltYWdlIGlkPSJpbWFnZTAiIHdpZHRoPSI0OCIgaGVpZ2h0PSI0OCIgeD0iMCIgeT0iMCIKICAgIGhyZWY9ImRhdGE6aW1hZ2UvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBREFBQUFBd0NBTUFBQUJnM0FtMUFBQUFCR2RCVFVFQUFMR1BDL3hoQlFBQUFDQmpTRkpOCkFBQjZKZ0FBZ0lRQUFQb0FBQUNBNkFBQWRUQUFBT3BnQUFBNm1BQUFGM0NjdWxFOEFBQUJPRkJNVkVYLy8vOVZ4aHhWeEJwU3hCdFMKeEJwVHhSdFN4UnRUeFJ0YTBoNVN4UnRUeFJwU3hScG16RE5TeFJ0U3hScFYvMVZTeFJ0U3hCdFZ4aHhUeFJwVHh4eFR4UnhUeEJ0Uwp4UnBUeGh4VnhoeFR4UnBUeFJwVHhpQlR4QnBTeGh4U3hodFR4UnRWeFJ0VHhocFR4UnRWeGh4VHhoeFV4aHRTeFJ4VHhCcFN4UjFUCnhSdFZ4UnRVeFJ0VHhCdFR4QjFUeGh4U3hCcFh5UnRUeFJwU3hScFR5aDcvLy85VHhodFN4UnBTeEJ0U3hSdFZ4QnAvLzM5VHhSeGIKeUJ0VXhSMVR4QnBYeFIxVHhSdFN4UnBWeUI1VHhScFR4QnRTeFNGLy8wQlR4QnRVeFJ4VnlCNVR4UnRWMUNwVHhCeFN4UnBWeWlCdAoyeVJTeFJ0VHhSdFR4UnBUeEJ0VnppUlR4UnBUeEJ0VnhoeGQwUzVUeFJ0U3hCdG16RE5peENkU3hCcFR4UnRnM3lCZ3p5QlR4QnRTCnhSdFR5QnhTeEJwU3hCci8vLy9lVmd0eUFBQUFablJTVGxNQUNVNlB0OXZzK1JGZ3IvVUtmUElEWnVZYnl6ZGM5LzZCTGFiOUtQeGEKZWZwVVltbEliME4yOHo3d09ZUHVOSXJyTDVIb0t3R1hmL2psSndLZEhFYmhJNk9JUFBIZEh3U3BnRFBhQm92V0dBZTBsdURSRmJtZwpFZ3UrcWdVTnc3TUlFTWk5SmN3NW1HOUdBQUFBQVdKTFIwUUFpQVVkU0FBQUFBZDBTVTFGQitRSEJ3STJNaXk1QXdZQUFBSFNTVVJCClZFakhsWmJsZ29Nd0RJQXpiQWZNbUxEYnVidTd1N3U3UzkvL0VTN0gyTEJDYVg2MTVVdEowMGdCL0pJUVJFbFdra2xGbGtRaEFReHAKVURXZHVFVFgxSVlJUEpYT2tJQmswcWtRUEpzekNGV01YSmJHNXdza1ZBcjVJRjgwU0lRWVJUOWZNa21rbUNVZlQ1amkwU2lhYkFYVApaVlhlWVBONGp2ckpzNFU0UFBxcTV0MWNQSjZRbkgyL3NReXlqS3JlZVRvdVQwamFpcmRNZklYTWZ5U3FUS3pjV0dteWh5b3FhQ3krCkdha1dlNnhodnVnTXZyVU5kMjIzSjNvQ0JBYmYwWWw4VjNOdEtvQVl6U2NGNUx2TDlia0lVaVJ2OWlEZjYxcVFRSTdpOVQ3ays5MHIKTWlnUi9NQmc0RjRWU0lielE4UElqL2pPRktFd09vYjh1TjhKNFNaTlRDSS81VjlWWElkdTlYeVpuZ0dZblF0c0l6dHVuVTkxTFRnZgpGcGNBbGxlQy81V2NpMXNGV0t1SHlmb0dKdUlteFZEUkNZMHROSGw3cHpyZTNRUFlQNkNkVEhBRjN5RnFIRmsrT01hL25WQ2pHSVBQCkZkNm5xREdEK1hwMkRuQnhTWFdkNWsyZ3ExbUFqZXViV3d6Nk83cXZWVitLM21NaFdYMEFlSHlpODFhS2VvTGwrZVYvNWZXTnpsZUwKZ0xmTXZIOEFmSDZGOEhhWjhSYXk3NS9LYndoZksyU3c3eW1WNVREY0taWGN4WmkvM1BNM0ZQNld4ZDhVK2RzdWYyTUg3cWVEMVN6NApIaWVXTUo0L2YydzVmK0FZWVI2V0FBQUFKWFJGV0hSa1lYUmxPbU55WldGMFpRQXlNREl3TFRBM0xUQTJWREU0T2pVME9qVXdLekE0Ck9qQXdKOWVjOVFBQUFDVjBSVmgwWkdGMFpUcHRiMlJwWm5rQU1qQXlNQzB3Tnkwd05sUXhPRG8xTkRvMU1Dc3dPRG93TUZhS0pFa0EKQUFBZ2RFVllkSE52Wm5SM1lYSmxBR2gwZEhCek9pOHZhVzFoWjJWdFlXZHBZMnN1YjNKbnZNOGRuUUFBQUJoMFJWaDBWR2gxYldJNgpPa1J2WTNWdFpXNTBPanBRWVdkbGN3QXhwLys3THdBQUFCZDBSVmgwVkdoMWJXSTZPa2x0WVdkbE9qcElaV2xuYUhRQU5EaUhZSWN0CkFBQUFGblJGV0hSVWFIVnRZam82U1cxaFoyVTZPbGRwWkhSb0FEUTRmODlIb0FBQUFCbDBSVmgwVkdoMWJXSTZPazFwYldWMGVYQmwKQUdsdFlXZGxMM0J1WnoreVZrNEFBQUFYZEVWWWRGUm9kVzFpT2pwTlZHbHRaUUF4TlRrME1ETXlPRGt3NHZva05BQUFBQkowUlZoMApWR2gxYldJNk9sTnBlbVVBTVRRM01FSkN1WDFYTkFBQUFFWjBSVmgwVkdoMWJXSTZPbFZTU1FCbWFXeGxPaTh2TDJGd2NDOTBiWEF2CmFXMWhaMlZzWXk5cGJXZDJhV1YzTWw4NVh6RTFPVEkxTlRnMU5EUXdORGN4TVRrM1h6YzJYMXN3WFJWVTFVOEFBQUFBU1VWT1JLNUMKWUlJPSIgPjwvaW1hZ2U+Cjwvc3ZnPgo=">
                      </div>
                    </div>
                    <span class="collection-info">
                    <p class="collection-edit-box">
                      <em class="line" @click="deleteCollection">删除收藏夹</em>
                      <!--                      <em class="line">移动收藏夹</em>-->
                    </p>
                  </span>
                  </div>
                  <div class="tab-content-box">
                    <div class="collection-box">
                      <div class="collection-input">
                        <el-input
                            size="small"
                            v-model="inputSearch"
                            placeholder="输入关键词"
                            @input="handleSelect(inputSearch)"
                            class="input-with-select">
                          <template #append>
                            <el-button>
                              <el-icon>
                                <search/>
                              </el-icon>
                            </el-button>
                          </template>
                        </el-input>
                      </div>
                    </div>
                  </div>
                </li>
                <ul class="collection-sublist">
                  <li v-for="i in filterIndex">
                    <div class="collection-con" style="width: 95%;">
                      <span class="collection-dir">
                        <el-tooltip content="移动收藏夹" placement="top" effect="light">

                        <el-icon size="16" color="#108ee9" style="margin-right:20px;cursor: pointer;bottom: 0"
                                 @click="dialogFormVisible = true;moveChooseIndex=i"><folder-remove/></el-icon>
                        </el-tooltip>
                          <em class="collection-type">BLOG</em>
                        <router-link :to="{name: 'BlogDetail', params: {blogId: this.filterBlogs[i].id}}"
                                     style="text-decoration: none">
                        <span class="subtitle" v-if="inputSearch" v-html="filterBlogs[i].title"></span>
                        <span class="subtitle" v-else>{{ filterBlogs[i].title }}</span>
                        </router-link>
                      </span>
                      <div class="collection-detail-right">
                        <a class="collect-cancel">
                          <img v-if="filterBlogs[i].collect" @click="cancelCollect(i,filterBlogs[i].id)"
                               src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4xIiBpZD0iTGF5ZXJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHdpZHRoPSIzMnB4IiBoZWlnaHQ9IjMycHgiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgMzIgMzIiIHhtbDpzcGFjZT0icHJlc2VydmUiPiAgPGltYWdlIGlkPSJpbWFnZTAiIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgeD0iMCIgeT0iMCIKICAgIGhyZWY9ImRhdGE6aW1hZ2UvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQ0FBQUFBZ0NBTUFBQUJFcElyR0FBQUFCR2RCVFVFQUFMR1BDL3hoQlFBQUFDQmpTRkpOCkFBQjZKZ0FBZ0lRQUFQb0FBQUNBNkFBQWRUQUFBT3BnQUFBNm1BQUFGM0NjdWxFOEFBQUJMMUJNVkVYLy8vLy96SGIvekhiL3kzYi8KekhiL3pKbi96WGIvekhmLzFZRC95M2IvekhiLzBYYi96WFgvejREL3kzYi96SGIveklEL3kzYi96SGYveklEL3pIWC8ySGIvekhiLwp5M2YvLzRELzBIbi96SGIveTNYL3pYYi8vNnIvMzREL3puYi96WGYvekhiL3kzYi96SFgveTNYL3kzYi96SGIvekhmL3puZi96WGIvCnkzYi95M1gvekhiL3pIZi96WGovelhiL3pIWC95M2IvekhYL3pIYi8wWGIveTNYL3pYai95M24vekhuL3kzYi95M1gvelhqLzBIai8KekhYL3ozci8wWHIvekhYL3pIZi96SG4vekhiL3pubi8wWUQvelhYL3puai96SFgvekhiL3pIYi8wWXYvekhiL3pIWC8vNEQveTNiLwp6blgvem52L3puYi95M2IvMjVMLzFZRC96SFgvMG5qL3pYWC96SGIvekhmLzQ0Ny95M1gvekhYLzI0RC96SGYveTNiL3kzYi96SGIvCnkzWC8vLzk5R2ZheUFBQUFZM1JTVGxNQWZlcnNqUVYxa0JqMytSeUpFUEwxRkh1REN1OE5ibllDSnVIbktRTUlOR1dWeGZUMnk1eHQKUG1iTy9OdHlVWERjM2E2ckovRWtPemMyN1RNeDZ6QXM2UzBvN2lvV1RFU3FvTm9ML2ZnRTQySWZHdWdIQnY0Um1IZDRDYzk4RGcrQQoxSnVzMXBSK0FBQUFBV0pMUjBRQWlBVWRTQUFBQUFkMFNVMUZCK1FIQWhVWE9vQksyeEFBQUFFWFNVUkJWRGpMWTJCQUFZeE16Q3lzCkRMZ0JXeklRc09PVzUrQUVLZURpeHFtQUp6a1pyeEc4ZkJBRi9BSTRGQWdtUTRFUWRubGhKcGdDRVZHc0NzU1M0VUFjaTdTRXBCUkMKZ2JTTUxMS2NuTHlDb3BKeU1ncFFVVlZUMStBRlMydHFhU2ZqQWpxNkRBeDZ5WGlCUG9NQmZnV0dEQ3I0RmFnd0dPRlhZTXhnb294UAozdFNNZ2NIY0ZMZThpQVhJbjVZaXVPU3RyQ0VCWmNPTVhkN1dEaGFVOWc3WTVCMGNFWUh0aE1VV1p4Zms2TERDWWdDeVBEYzJLMXlSCkZMaGhVK0NPTGJFaEExMGtCUjdZRkhnaUtmRENwa0FMSWUvdGcwMkJyeDljZ1NOYzBGOHpBSkUwQStFS2dxQWl3V0loREF5aDZzRVEKWGxnNHdvNElrQUJ6cERBMGcwUTVnL2pJT1RCYWpTa21OZzdCajA5SUZFa0tBVE1CV3Zqa0kxUmlUaDhBQUFBbGRFVllkR1JoZEdVNgpZM0psWVhSbEFESXdNakF0TURjdE1ESlVNVE02TWpNNk5UZ3JNRGc2TUREc0p5OU1BQUFBSlhSRldIUmtZWFJsT20xdlpHbG1lUUF5Ck1ESXdMVEEzTFRBeVZERXpPakl6T2pVNEt6QTRPakF3blhxWDhBQUFBQ0IwUlZoMGMyOW1kSGRoY21VQWFIUjBjSE02THk5cGJXRm4KWlcxaFoybGpheTV2Y21lOHp4MmRBQUFBR0hSRldIUlVhSFZ0WWpvNlJHOWpkVzFsYm5RNk9sQmhaMlZ6QURHbi83c3ZBQUFBRjNSRgpXSFJVYUhWdFlqbzZTVzFoWjJVNk9raGxhV2RvZEFBek1pajArUFFBQUFBV2RFVllkRlJvZFcxaU9qcEpiV0ZuWlRvNlYybGtkR2dBCk16TFFXemg1QUFBQUdYUkZXSFJVYUhWdFlqbzZUV2x0WlhSNWNHVUFhVzFoWjJVdmNHNW5QN0pXVGdBQUFCZDBSVmgwVkdoMWJXSTYKT2sxVWFXMWxBREUxT1RNMk5qYzBNemp6RkI4NUFBQUFFWFJGV0hSVWFIVnRZam82VTJsNlpRQTVNREJDUXJPOW5IVUFBQUJHZEVWWQpkRlJvZFcxaU9qcFZVa2tBWm1sc1pUb3ZMeTloY0hBdmRHMXdMMmx0WVdkbGJHTXZhVzFuZG1sbGR6SmZPVjh4TlRreU5UVTROVFF6Ck5UTTJOVEUxTWw4eU9WOWJNRjBnc0hhZkFBQUFBRWxGVGtTdVFtQ0MiID48L2ltYWdlPgo8L3N2Zz4K">
                          <img v-else
                               src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+CjxzdmcgdmVyc2lvbj0iMS4xIiBpZD0iTGF5ZXJfMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiIHdpZHRoPSIzMnB4IiBoZWlnaHQ9IjMycHgiIHZpZXdCb3g9IjAgMCAzMiAzMiIgZW5hYmxlLWJhY2tncm91bmQ9Im5ldyAwIDAgMzIgMzIiIHhtbDpzcGFjZT0icHJlc2VydmUiPiAgPGltYWdlIGlkPSJpbWFnZTAiIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiIgeD0iMCIgeT0iMCIKICAgIGhyZWY9ImRhdGE6aW1hZ2UvcG5nO2Jhc2U2NCxpVkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQ0FBQUFBZ0NBUUFBQURaYzdKL0FBQUFCR2RCVFVFQUFMR1BDL3hoQlFBQUFDQmpTRkpOCkFBQjZKZ0FBZ0lRQUFQb0FBQUNBNkFBQWRUQUFBT3BnQUFBNm1BQUFGM0NjdWxFOEFBQUFBbUpMUjBRQS80ZVB6TDhBQUFBSGRFbE4KUlFma0J3SVZHd0lFL1N5Q0FBQUI5VWxFUVZSSXg2VlZUV3NUVVJROTk5azBIODNISkdBUmxBcTJvZ3RwSXdWWExpUkNYZnNiRkQ4UQpMQVZGRndyK0FjV2RwWW8vd1oyZzlBY1VDcUZHVkZTQ1N1bkNoc3g3NDBRYjIzeGNGeWJPWkh3VDNxVDNiV1llNTV3NTU4MjdYRERDCmw3b3ZhN0t1SG5Jc0hET0VMbS9iM0Z1UHcxSEUwTmV2UTd0ZmtmajdUSHVKNmVTV0hpZEMrTmhiNnRNQkhtL2VDc09GT0hDczdpWXkKdm8yZDJORk1QWUlEdmpGQUIxS3R4U2dPVXVvYkh3d0FuZndVR29ZT25NdEJPc0NXdW03bVFEUm0ycXM4cGNGK0h5K2xQNk9qRjBqOApPTlU1VFVVVWVRNFRDSzhtM3FNaUtxamdyZVgwQk5RVlhNTXNIMERFb2lvL0tUd2lkYkg3SWlyVmwzZUpaSlduUnhlZ0wyVHZJRG02CkFKb0NyL1pCQjE2U2U2SlZIbnJ1dzhvZG14ZVpUM1FCN2tqNW5iR0ZiSlVZZ0h1bTg1cXRpSFFwRm5MbDNsWE9ybE9KN0VqME9wM1AKbFlGL3ZXQnQ0QnhxeHZ3YVN0YWJucFRYQzQyVDdUV1RJS1JpWjlNZittKytic3g4Uk5mazg5enk2QU1DelNOY01Bb3crWE5TSy9DNwphSG9FblZtdEFPYjJLY0RHQWdoeFlCeUJmQUxlYjV4UUxndERnZDE4R3UyQUEzVk1ROStpcStJU2JRYTNPZTRjL2k5Q1BIaVZ0OFZpCllTYS9ZajNQSHhjM3NUM2dnTWxyUDk4d1hlNFBVMW1YZHpnMU1FUlQ4cTZVdW1IckJ5WHRaN0ltTjlROXptcW5kYzUrSU45SnBaNXkKM052OUF5ZmVHbEhZaEhSR0FBQUFKWFJGV0hSa1lYUmxPbU55WldGMFpRQXlNREl3TFRBM0xUQXlWREV6T2pJM09qQXlLekE0T2pBdwpDVnplSEFBQUFDVjBSVmgwWkdGMFpUcHRiMlJwWm5rQU1qQXlNQzB3Tnkwd01sUXhNem95Tnpvd01pc3dPRG93TUhnQlpxQUFBQUFnCmRFVllkSE52Wm5SM1lYSmxBR2gwZEhCek9pOHZhVzFoWjJWdFlXZHBZMnN1YjNKbnZNOGRuUUFBQUJoMFJWaDBWR2gxYldJNk9rUnYKWTNWdFpXNTBPanBRWVdkbGN3QXhwLys3THdBQUFCZDBSVmgwVkdoMWJXSTZPa2x0WVdkbE9qcElaV2xuYUhRQU16SW85UGowQUFBQQpGblJGV0hSVWFIVnRZam82U1cxaFoyVTZPbGRwWkhSb0FETXkwRnM0ZVFBQUFCbDBSVmgwVkdoMWJXSTZPazFwYldWMGVYQmxBR2x0CllXZGxMM0J1WnoreVZrNEFBQUFYZEVWWWRGUm9kVzFpT2pwTlZHbHRaUUF4TlRrek5qWTNOakl5Q1Y0VENBQUFBQkYwUlZoMFZHaDEKYldJNk9sTnBlbVVBT0RZd1FrS3J0dW9aQUFBQVJuUkZXSFJVYUhWdFlqbzZWVkpKQUdacGJHVTZMeTh2WVhCd0wzUnRjQzlwYldGbgpaV3hqTDJsdFozWnBaWGN5WHpsZk1UVTVNalUxT0RVME1EQTFPVE0xTWpaZk1UUmZXekJkR0c2Tk1nQUFBQUJKUlU1RXJrSmdnZz09IiA+PC9pbWFnZT4KPC9zdmc+Cg==">
                        </a>
                      </div>
                    </div>
                  </li>
                  <el-pagination v-if="totalSize>2" class="mpage"
                                 background
                                 layout="prev, pager, next"
                                 :current-page=currentPage
                                 :page-size=pageSize
                                 @current-change=page
                                 :total="totalSize">
                  </el-pagination>
                </ul>
              </ul>


              <el-dialog v-model="dialogFormVisible" title="移动至其他收藏夹" width="300px">
                <el-select v-model="value" filterable placeholder="选择其他收藏夹">
                  <el-option
                      v-for="item in options"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
                <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="moveCollection">确定</el-button>
      </span>
                </template>
              </el-dialog>

            </div>
          </div>
        </div>
      </div>
    </div>
    <FloatButton/>
  </div>

</template>

<script>
import {Search, FolderRemove} from '@element-plus/icons'
import {ref, defineComponent, getCurrentInstance} from 'vue'
import FloatButton from "@/components/FloatButton";

export default defineComponent({
  name: "Collection",
  components: {
    Search,
    FolderRemove,
    FloatButton,
  },
  watch: {
    filterBlogs: {
      handler(val) {

      },
    }
  },
  data() {
    return {
      userId: 1,
      dialogFormVisible: false,
      dialogNewVisible:false,
      inputNewCollection:"",
      moveChooseIndex: 1,
      blogs: [
        {id: 1, title: " ", collect: true},
      ],
      filterBlogs: [
        {id: 1, title: " ", collect: true},
      ],
      filterIndex: [0,],
      options: [{id: 1, name: '  ',},
      ],
      currentPage:1,
      totalSize:0,
      pageSize:10,
      value: "",
      inputSearch: "",
      folderName: "",
      editCollectionName: true,
      favorites: [{id: 1, name: "  ", num: 5},
      ],
      currentIndex: 0,//当前选择的索引
    }
  },
  methods: {
    page(currentPage) {
      this.totalSize=this.filterBlogs.length
      this.filterIndex = [];
      const min = (currentPage - 1) * this.pageSize;
      const max = min + this.pageSize
      for (let i = min; i < this.totalSize && i < max; i++) {
          this.filterIndex.push(i)
      }
      this.currentPage = currentPage;
    },
    async deleteCollection(){
      const favorite=this.favorites[this.currentIndex]
      if(favorite.num>0){
        this.$message.error('该收藏夹还有文章，不能删除');
      }else{
        const r1=await this.$axios('/favorite/delete/'+favorite.id)
        console.log(r1)
        this.favorites.splice(this.currentIndex,this.currentIndex+1)
        this.clickFavorites(0);
      }
    },
    async getFavorite(){
      const _this=this
      const r1=await this.$axios('/favorite/list/' + this.userId).then(res => {
        _this.favorites = res.data.object.favoriteList;
        console.log("getFavorite")
      });
      console.log(r1)
      this.clickFavorites(0);
      this.options={...this.favorites};
    },
    async createFavorite(){
      if(this.isNotExist(this.inputNewCollection)){
        const _this=this
        var formData=new FormData();
        formData.append("favoriteName",this.inputNewCollection);
        const r1= await this.$axios.post('/favorite/create/', formData).then(res => {
          _this.inputNewCollection = ''
        })
        console.log(r1)
        this.getFavorite().then(r => {})
        this.dialogNewVisible=false
      }
    },
    clickFavorites(index) {
      this.currentIndex = index;
      const _this = this;
      this.$axios('/favoriteinfo/list/' + this.favorites[index].id + '?userId=' + this.userId).then(res => {
        _this.blogs = res.data.object.favoriteList;
        _this.filterBlogs = _this.blogs;
        this.reSetFilterIndex();
      })
    },
    async moveCollection() {
      this.dialogFormVisible = false
      let favoriteInfo={favoriteId:this.value,blogId:this.filterBlogs[this.moveChooseIndex].id, userId:this.userId}
      await this.$axios.post('/favoriteinfo/update/'+this.favorites[this.currentIndex].id,favoriteInfo)
      this.getFavorite().then()
    },
    async updateFolderName() {
      const favorite=this.favorites[this.currentIndex]
      if(this.folderName==favorite.name){
        this.editCollectionName = true;
        return;
      }
      if(this.isNotExist(this.folderName)){
        favorite.name=this.folderName
        const r1=await this.$axios.post('/favorite/update',favorite)
        this.editCollectionName = true;
        console.log(r1)
      }
    },
    cancelCollect(index, id) {
      this.filterBlogs[index].collect = false;
      //根据id取消后端收藏,blog收藏数-1，删除favoriteinfo一条记录，favorite的收藏数-1
      //收藏成功，favorite的收藏数+1
      this.$axios('/favoriteinfo/delete/?blogId=' + id)
    },
    handleSelect(input) {
      if (input) {
        // 忽略输入大小写
        input = new RegExp(input, 'i');
        // 在所有文章作者、标题、描述中匹配查询值
        var index = 0;
        var find = false;
        this.filterBlogs = [];
        for (let i = 0; i < this.blogs.length; i++) {
          var mappingString = this.blogs[i].title;
          if (mappingString.search(input) !== -1) {
            find = true;
            var indexContent = mappingString.search(input);
            var l = input.toString().length - 3;
            // 将匹配到内容的地方进行黄色标记，并包括周围一定数量的文本
            mappingString = mappingString.slice(0, indexContent) +
                '<mark>' + mappingString.slice(indexContent, indexContent + l) + '</mark>' +
                mappingString.slice(indexContent + l, -1);
          }
          if (find) {
            find = false;
            this.filterBlogs[index] = {...this.blogs[i]};
            this.filterBlogs[index++].title = mappingString;
          }
        }
        this.reSetFilterIndex();
      } else {
        this.filterBlogs = this.blogs
      }
    },

    async mainFuction() {
      const r1 = await this.getFavorite()

    },
    reSetFilterIndex() {
      this.page(1);
    },
    isNotExist(input) {
      for (let i = 0; i < this.favorites.length; i++) {
        if (this.favorites[i].name === input) {
          this.$message.error('名称已存在');
          return false;
        }
      }
      return true;
    },
  },
  created() {
    this.userId = this.$route.params.userId
    this.mainFuction();
  },
})
</script>

<style scoped>


.collection {
  background-color: #f6f7f8;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  min-height: calc(100% - 140px);
}

.c-container {
  position: relative;
  width: auto;
  min-width: 60%;
  min-height: 90%;
  background-color: transparent;
  margin: 10px auto 0;
}

.container-box {
  min-width: 1000px;
  min-height: 800px;
}

.container-white {
  width: 1000px;
  background: #fff;
  overflow: initial !important;
}

.c-left-tab {
  min-height: 760px;
  width: 200px;
  border-right: 1px solid #ededed;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  float: left;
}

.c-left-tab li:hover {
  background: #f6f7f8;
  font-weight: 500;
}

.c-right-content {
  min-height: 400px;
  overflow: hidden;
}

li {
  list-style: none;
}

.menu-item {
  height: 49px;
  padding-left: 16px;
  border-bottom: 1px solid #ededed;
  position: relative;
  padding-right: 8px;
  padding-top: 10px;
  cursor: pointer;
  font-weight: 400;
}

.menu-item-new {
  line-height: 35px;
  padding-top: 16px !important;
}

.imgBox {
  width: 16px;
  height: 16px;
  display: inline-block;
  background: url(data:image/png;base64,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) no-repeat;
  background-size: 32px 80px;
  background-position: 0 0;
  position: relative;
  top: 3px;
}

.menu-item span {
  font-family: PingFangSC-Regular, PingFang SC;
  font-size: 14px;
  color: #555666;
  line-height: 16px;
  margin-left: 8px;
}

.menu-item-title .imgBox {
  background-position: 0 -16px;
}

.active-menu-item .menu-item-title .imgBox {
  background-position: -16px -16px;
}

.active-menu-item {
  position: relative;
  background: #f6f7f8;
}

.active-menu-item .menu-item-like .imgBox {
  background-position: -16px -64px;
}

.menu-item-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.menu-item-like {
  position: absolute;
  bottom: 12px;
  right: 8px;
}

.comment .imgBox {
  background-position: 0 -64px;
}

.collection .list-wrap {
  min-height: 760px;
}

.list-wrap ul {
  border: 0;
  font-size: 100%;
  vertical-align: baseline;
  margin: 0;
  padding: 0;
  list-style: none;
  font-family: PingFang SC, Hiragino Sans GB, Arial, Microsoft YaHei, Verdana, Roboto, Noto, Helvetica Neue, sans-serif !important;
}

.collection-list li {
  position: relative;
  padding: 16px 0;
  border-bottom: 1px solid #eaebec;
  line-height: 20px;
  border: none;
  list-style: none;
}

.titleHead {
  padding: 0 16px;
  height: 85px;
  border-bottom: 1px solid #ededed;
  position: relative;
}

.tab-content-box {
  margin-left: 16px;
  margin-right: 16px;
  height: 48px;
  border-bottom: 1px solid #ededed;
  position: relative;
}

.collection-list .collection-sublist {
  margin-top: 8px;
  margin-bottom: -17px;
  padding: 0 16px;
}

.collection-dir {
  font-size: 14px !important;
  color: #555666;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  width: 100%;
}

.collection-folder-name img {
  width: 16px;
  top: 3px;
  position: relative;
  left: 8px;
  cursor: pointer;
}

.collection-info {
  font-size: 14px;
  overflow: hidden;
  margin-top: 10px;
  width: 100%;
  height: 33px;
  display: block;
}

.collection-edit-box {
  float: left;
  width: 300px;
  text-align: left;
  margin: 0;
  padding: 0;
}

.collection-info em {
  color: #108ee9;
  font-style: normal;
  cursor: pointer;
  display: inline-block;
  margin-right: 10px;
  font-weight: 400;
  vertical-align: unset;
  font-size: 14px;
}

.line:after {
  display: inline-block;
  margin-left: 10px;
  height: 12px;
  width: 1px;
  vertical-align: middle;
  content: "";
  font-size: 0;
  background-color: #d8d8d8;
}

.collection-box {
  padding: 4px;
  border-radius: 4px;
  position: absolute;
  left: 0;
  top: 50%;
  -webkit-transform: translateY(-50%);
  transform: translateY(-50%);
}

.collection-input {
  width: 220px;
  height: 32px;
  border-radius: 4px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.collection-con {
  position: relative;
  padding: 0 24px;
  background: #fff;
  display: inline-block;
  width: 100%;
  font-size: 14px !important;
  line-height: 52px;
}

.collection-detail-right {
  position: absolute;
  right: 0;
  width: 150px;
  height: 100%;
  top: 0;
  text-align: right;
  line-height: 52px;
  padding-right: 18px;
}

.collection-type {
  height: 20px;
  padding: 0 5px;
  border: 1px solid #d5d5d5;
  background-color: #fff;
  color: #999;
  font-size: 12px;
  vertical-align: top;
  font-weight: 400 !important;
  border-radius: 4px;
  font-style: normal;
  line-height: 52px;
}

.collection-sublist li {
  padding: 0;
  position: relative;
  margin-top: 0;
  background: #fff;
  border: 1px solid #ededed;
  border-radius: 4px;
  margin-bottom: 8px;
  height: 52px;
  overflow: hidden;
  line-height: 52px;
}

.collection-sublist li:hover {
  box-shadow: 0 2px 5px 0 rgb(0 0 0 / 10%);
}

.collection-sublist li:hover .subtitle {
  color: #fc5531;
  text-shadow: 0 2px 5px rgb(0 0 0 / 10%);
}

.subtitle {
  font-family: Helvetica !important;
  color: #555666;
  display: inline;
  cursor: pointer;
  margin-left: 5px;
}

.collect-cancel {
  margin-right: 10px;
  cursor: pointer;
}

.collection-detail-right img {
  width: 16px;
  position: relative;
  top: 3px;
  left: 6px;
}

.input-edit-box {
  width: 400px;
  height: 32px;
}

.collection-edit-name {
  width: 300px;
  float: left;
  position: relative;
  font-size: 14px;
  display: inline-block;
  line-height: 32px;
}

.collection-edit-name >>> .el-input__inner {
  height: 32px !important;
  line-height: 32px !important;
}

.input-edit-box img {
  display: block;
  float: left;
  width: 24px;
  margin: 4px 0 0 15px;
  cursor: pointer;
}


@media (max-width: 992px) {
  .container-box {
    min-width: 500px;
    min-height: 600px;
  }

  .container-white {
    width: 500px;
  }

  .c-left-tab {
    min-height: 600px;
    width: 150px;
  }

  .collection .list-wrap {
    min-height: 600px;
  }

  .collection-edit-box {
    width: 150px;
  }

  .collection-input {
    height: 16px;
  }

  .menu-item span {
    font-size: 12px;
  }

  .collect-cancel {
    margin-right: 30px;
  }

  .input-edit-box {
    width: 300px;
  }

  .collection-edit-name {
    width: 200px;
  }

  .collection-box {
    top: 30%;
  }
}
</style>