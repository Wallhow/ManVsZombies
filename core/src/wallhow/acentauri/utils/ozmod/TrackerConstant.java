/*
OZMod - Java Sound Library
Copyright (C) 2011  Igor Kravtchenko

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

Contact the author: igor@tsarevitch.org
*/

package wallhow.acentauri.utils.ozmod;

public class TrackerConstant {

    static int squareTable[] = {
        255, 255, 255, 255, 255, 255, 255, 255,
        255, 255, 255, 255, 255, 255, 255, 255,
        255, 255, 255, 255, 255, 255, 255, 255,
        255, 255, 255, 255, 255, 255, 255, 255,
        -255, -255, -255, -255, -255, -255, -255, -255,
        -255, -255, -255, -255, -255, -255, -255, -255,
        -255, -255, -255, -255, -255, -255, -255, -255,
        -255, -255, -255, -255, -255, -255, -255, -255,
    };

    static int vibratoTable[] = {
	0,24,49,74,97,120,141,161,
	180,197,212,224,235,244,250,253,
	255,253,250,244,235,224,212,197,
	180,161,141,120,97,74,49,24,

	0,-24,-49,-74,-97,-120,-141,-161,
	-180,-197,-212,-224,-235,-244,-250,-253,
	-255,-253,-250,-244,-235,-224,-212,-197,
	-180,-161,-141,-120,-97,-74,-49,-24
    };

    static int finetune[] = {
        8363,   8413,   8463,   8529,   8581,   8651,   8723,   8757,
        7895,   7941,   7985,   8046,   8107,   8169,   8232,   8280
    };

    static int defaultPeriod[] = {
        1712,1616,1524,1440,1356,1280,1208,1140,1076,1016,960,906,
        856,808,762,720,678,640,604,570,538,508,480,453,
        428,404,381,360,339,320,302,285,269,254,240,226,
        214,202,190,180,170,160,151,143,135,127,120,113,
        107,101,95,90,85,80,75,71,67,63,60,56
    };


    static int lintab[] = {
        16726,16741,16756,16771,16786,16801,16816,16832,16847,16862,16877,16892,16908,16923,16938,16953,
        16969,16984,16999,17015,17030,17046,17061,17076,17092,17107,17123,17138,17154,17169,17185,17200,
        17216,17231,17247,17262,17278,17293,17309,17325,17340,17356,17372,17387,17403,17419,17435,17450,
        17466,17482,17498,17513,17529,17545,17561,17577,17593,17608,17624,17640,17656,17672,17688,17704,
        17720,17736,17752,17768,17784,17800,17816,17832,17848,17865,17881,17897,17913,17929,17945,17962,
        17978,17994,18010,18027,18043,18059,18075,18092,18108,18124,18141,18157,18174,18190,18206,18223,
        18239,18256,18272,18289,18305,18322,18338,18355,18372,18388,18405,18421,18438,18455,18471,18488,
        18505,18521,18538,18555,18572,18588,18605,18622,18639,18656,18672,18689,18706,18723,18740,18757,
        18774,18791,18808,18825,18842,18859,18876,18893,18910,18927,18944,18961,18978,18995,19013,19030,
        19047,19064,19081,19099,19116,19133,19150,19168,19185,19202,19220,19237,19254,19272,19289,19306,
        19324,19341,19359,19376,19394,19411,19429,19446,19464,19482,19499,19517,19534,19552,19570,19587,
        19605,19623,19640,19658,19676,19694,19711,19729,19747,19765,19783,19801,19819,19836,19854,19872,
        19890,19908,19926,19944,19962,19980,19998,20016,20034,20052,20071,20089,20107,20125,20143,20161,
        20179,20198,20216,20234,20252,20271,20289,20307,20326,20344,20362,20381,20399,20418,20436,20455,
        20473,20492,20510,20529,20547,20566,20584,20603,20621,20640,20659,20677,20696,20715,20733,20752,
        20771,20790,20808,20827,20846,20865,20884,20902,20921,20940,20959,20978,20997,21016,21035,21054,
        21073,21092,21111,21130,21149,21168,21187,21206,21226,21245,21264,21283,21302,21322,21341,21360,
        21379,21399,21418,21437,21457,21476,21496,21515,21534,21554,21573,21593,21612,21632,21651,21671,
        21690,21710,21730,21749,21769,21789,21808,21828,21848,21867,21887,21907,21927,21946,21966,21986,
        22006,22026,22046,22066,22086,22105,22125,22145,22165,22185,22205,22226,22246,22266,22286,22306,
        22326,22346,22366,22387,22407,22427,22447,22468,22488,22508,22528,22549,22569,22590,22610,22630,
        22651,22671,22692,22712,22733,22753,22774,22794,22815,22836,22856,22877,22897,22918,22939,22960,
        22980,23001,23022,23043,23063,23084,23105,23126,23147,23168,23189,23210,23230,23251,23272,23293,
        23315,23336,23357,23378,23399,23420,23441,23462,23483,23505,23526,23547,23568,23590,23611,23632,
        23654,23675,23696,23718,23739,23761,23782,23804,23825,23847,23868,23890,23911,23933,23954,23976,
        23998,24019,24041,24063,24084,24106,24128,24150,24172,24193,24215,24237,24259,24281,24303,24325,
        24347,24369,24391,24413,24435,24457,24479,24501,24523,24545,24567,24590,24612,24634,24656,24679,
        24701,24723,24746,24768,24790,24813,24835,24857,24880,24902,24925,24947,24970,24992,25015,25038,
        25060,25083,25105,25128,25151,25174,25196,25219,25242,25265,25287,25310,25333,25356,25379,25402,
        25425,25448,25471,25494,25517,25540,25563,25586,25609,25632,25655,25678,25702,25725,25748,25771,
        25795,25818,25841,25864,25888,25911,25935,25958,25981,26005,26028,26052,26075,26099,26123,26146,
        26170,26193,26217,26241,26264,26288,26312,26336,26359,26383,26407,26431,26455,26479,26502,26526,
        26550,26574,26598,26622,26646,26670,26695,26719,26743,26767,26791,26815,26839,26864,26888,26912,
        26937,26961,26985,27010,27034,27058,27083,27107,27132,27156,27181,27205,27230,27254,27279,27304,
        27328,27353,27378,27402,27427,27452,27477,27502,27526,27551,27576,27601,27626,27651,27676,27701,
        27726,27751,27776,27801,27826,27851,27876,27902,27927,27952,27977,28003,28028,28053,28078,28104,
        28129,28155,28180,28205,28231,28256,28282,28307,28333,28359,28384,28410,28435,28461,28487,28513,
        28538,28564,28590,28616,28642,28667,28693,28719,28745,28771,28797,28823,28849,28875,28901,28927,
        28953,28980,29006,29032,29058,29084,29111,29137,29163,29190,29216,29242,29269,29295,29322,29348,
        29375,29401,29428,29454,29481,29507,29534,29561,29587,29614,29641,29668,29694,29721,29748,29775,
        29802,29829,29856,29883,29910,29937,29964,29991,30018,30045,30072,30099,30126,30154,30181,30208,
        30235,30263,30290,30317,30345,30372,30400,30427,30454,30482,30509,30537,30565,30592,30620,30647,
        30675,30703,30731,30758,30786,30814,30842,30870,30897,30925,30953,30981,31009,31037,31065,31093,
        31121,31149,31178,31206,31234,31262,31290,31319,31347,31375,31403,31432,31460,31489,31517,31546,
        31574,31602,31631,31660,31688,31717,31745,31774,31803,31832,31860,31889,31918,31947,31975,32004,
        32033,32062,32091,32120,32149,32178,32207,32236,32265,32295,32324,32353,32382,32411,32441,32470,
        32499,32529,32558,32587,32617,32646,32676,32705,32735,32764,32794,32823,32853,32883,32912,32942,
        32972,33002,33031,33061,33091,33121,33151,33181,33211,33241,33271,33301,33331,33361,33391,33421
    };


    static final int LOGFAC = 2*16;

    static int logtab[] = {
        LOGFAC*907, LOGFAC*900, LOGFAC*894, LOGFAC*887, LOGFAC*881, LOGFAC*875, LOGFAC*868, LOGFAC*862,
        LOGFAC*856, LOGFAC*850, LOGFAC*844, LOGFAC*838, LOGFAC*832, LOGFAC*826, LOGFAC*820, LOGFAC*814,
        LOGFAC*808, LOGFAC*802, LOGFAC*796, LOGFAC*791, LOGFAC*785, LOGFAC*779, LOGFAC*774, LOGFAC*768,
        LOGFAC*762, LOGFAC*757, LOGFAC*752, LOGFAC*746, LOGFAC*741, LOGFAC*736, LOGFAC*730, LOGFAC*725,
        LOGFAC*720, LOGFAC*715, LOGFAC*709, LOGFAC*704, LOGFAC*699, LOGFAC*694, LOGFAC*689, LOGFAC*684,
        LOGFAC*678, LOGFAC*675, LOGFAC*670, LOGFAC*665, LOGFAC*660, LOGFAC*655, LOGFAC*651, LOGFAC*646,
        LOGFAC*640, LOGFAC*636, LOGFAC*632, LOGFAC*628, LOGFAC*623, LOGFAC*619, LOGFAC*614, LOGFAC*610,
        LOGFAC*604, LOGFAC*601, LOGFAC*597, LOGFAC*592, LOGFAC*588, LOGFAC*584, LOGFAC*580, LOGFAC*575,
        LOGFAC*570, LOGFAC*567, LOGFAC*563, LOGFAC*559, LOGFAC*555, LOGFAC*551, LOGFAC*547, LOGFAC*543,
        LOGFAC*538, LOGFAC*535, LOGFAC*532, LOGFAC*528, LOGFAC*524, LOGFAC*520, LOGFAC*516, LOGFAC*513,
        LOGFAC*508, LOGFAC*505, LOGFAC*502, LOGFAC*498, LOGFAC*494, LOGFAC*491, LOGFAC*487, LOGFAC*484,
        LOGFAC*480, LOGFAC*477, LOGFAC*474, LOGFAC*470, LOGFAC*467, LOGFAC*463, LOGFAC*460, LOGFAC*457,
        LOGFAC*453, LOGFAC*450, LOGFAC*447, LOGFAC*443, LOGFAC*440, LOGFAC*437, LOGFAC*434, LOGFAC*431
    };
}
