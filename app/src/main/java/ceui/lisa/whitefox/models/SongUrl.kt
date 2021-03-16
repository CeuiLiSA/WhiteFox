package ceui.lisa.whitefox.models

class SongUrl {
    /**
     * data : [{"id":316654,"url":"http://m8.music.126.net/20210316144313/bdfeae64eeeb62877e1e86fec2061a92/ymusic/0376/4fc2/e837/e9fd4802a575aadbad8b084f1a75269e.mp3","br":128000,"size":3467850,"md5":"e9fd4802a575aadbad8b084f1a75269e","code":200,"expi":1200,"type":"mp3","gain":0,"fee":8,"uf":null,"payed":1,"flag":4,"canExtend":false,"freeTrialInfo":null,"level":"standard","encodeType":"mp3","freeTrialPrivilege":{"resConsumable":false,"userConsumable":false},"freeTimeTrialPrivilege":{"resConsumable":false,"userConsumable":false,"type":0,"remainTime":0},"urlSource":0}]
     * code : 200
     */
    var data: List<UrlData>? = null
    var code: Int? = null
}