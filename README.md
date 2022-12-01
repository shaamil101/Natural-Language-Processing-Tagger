# Natural-Language-Processing-Tagger
## The goal of this problem set is to tackle part of the speech understanding problem that a digital assistant such as Alexa or Siri would have to handle. We'll be using Hidden Markov Models here.

A single word can have different tags in different contexts; e.g., "present" could be a verb ("I'd like to present you with this opportunity to write some great code"); an adjective ("it'll be easier if you're present at section meeting"); or a noun ("no time like the present to get started on it"). A good tagger makes use of the context to disambiguate the possibilities and determine the correct label. The tagging is in turn a core part of having a "Sudi" understand what a sentence means and how to respond.A part of speech (POS) tagger labels each word in a sentence with its part of speech (noun, verb, etc.). For example:<img width="1125" alt="Screenshot 2022-11-30 at 10 36 59 PM" src="https://user-images.githubusercontent.com/40500380/204960157-ae70b415-cfb9-40c5-a681-da6b00b1c7f4.png">
So we see that "The" is a "DET" (determiner), "Fulton" an "NP" (proper noun), "County" a "N" (noun), etc. The punctuation is its own tag.

The tags that we'll use for this problem set are taken from the book Natural Language Processing with Python:

Tag	Meaning	Examples
ADJ	adjective	new, good, high, special, big, local
ADV	adverb	really, already, still, early, now
CNJ	conjunction	and, or, but, if, while, although
DET	determiner	the, a, some, most, every, no
EX	existential	there, there's
FW	foreign word	dolce, ersatz, esprit, quo, maitre
MOD	modal verb	will, can, would, may, must, should
N	noun	year, home, costs, time, education
NP	proper noun	Alison, Africa, April, Washington
NUM	number	twenty-four, fourth, 1991, 14:24
PRO	pronoun	he, their, her, its, my, I, us
P	preposition	on, of, at, with, by, into, under
TO	the word to	to
UH	interjection	ah, bang, ha, whee, hmpf, oops
V	verb	is, has, get, do, make, see, run
VD	past tense	said, took, told, made, asked
VG	present participle	making, going, playing, working
VN	past participle	given, taken, begun, sung
WH	wh determiner	who, which, when, what, where, how


We'll be taking the statistical approach to tagging, which means we need data. Fortunately the problem has been studied a great deal, and there exist good datasets. One prominent one is the Brown corpus (extra credit: outdo it with a Dartmouth corpus), covering a range of works from the news, fictional stories, science writing, etc. It was annotated with a more complex set of tags, which have subsequently been distilled down to the ones above.

The goal of POS tagging is to take a sequence of words and produce the corresponding sequence of tags.
## Using Hidden Markov Models and Viterbi Algorithm
<img width="550" alt="Screenshot 2022-11-30 at 10 38 29 PM" src="https://user-images.githubusercontent.com/40500380/204960350-8b6d9057-690d-4e58-9fea-5df6e8fc100f.png">
