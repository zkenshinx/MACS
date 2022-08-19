using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <string.h>

const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

typedef struct {
  string player;
  const void* actorFile;
} actorStruct;

typedef struct {
  film movie;
  const void* movieFile;
} movieStruct;

int imdb::compAct(const void *a, const void *b) {
  int *actorFileStart = (int*) ((actorStruct*)a)->actorFile;
  return strcmp( ((actorStruct*)a)->player.c_str(), (char*)actorFileStart + *(int*)b );
}


bool imdb::getCredits(const string& player, vector<film>& films) const { 
  int *actorFileStart = (int*)actorFile; 
  int actorsNum = *actorFileStart; // Number of actors 
  
  actorStruct as;
  as.player = player;
  as.actorFile = actorFile;

  void* res = (char*) bsearch(&as, actorFileStart + 1, actorsNum, sizeof(int), compAct);
  if (res == NULL) return false;
  char* result = (char*)actorFileStart + *(int*)res;
  // Calculates the base address where the actors number of movies resides
  int playerInfoSize = player.size() + (player.size() % 2 == 0 ? 2 : 1);
  char *movieCountPtr = result + playerInfoSize;
  short movieCount = *(short*)movieCountPtr; // Number of actor movies

  // Calculates the base address where the movies of actor start
  playerInfoSize += 2;
  if (playerInfoSize % 4 != 0) playerInfoSize += 2;
  int *movieStartPtr = (int*)(result + playerInfoSize);
  
  // Fills the films vector with the actors movies
  for (short i = 0; i < movieCount; i++) {
    string movieName = (char*)movieFile + *(movieStartPtr + i);
    short year = 1900 + *((char*)movieFile + *(movieStartPtr + i) + movieName.size() + 1);
    film currFilm;
    currFilm.title = movieName;
    currFilm.year = year;
    films.push_back(currFilm);
  }
  return true; 
}

int imdb::compMov(const void *a, const void *b) {
  int *movieFileStart = (int*)((movieStruct*)a)->movieFile;
  film myMovie = ((movieStruct*)a)->movie;
  film currMovie;
  currMovie.title = (char*)movieFileStart + *(int*)b;
  currMovie.year = 1900 + *( ((char*)movieFileStart + *(int*)b) + currMovie.title.size() + 1);
  if (myMovie == currMovie) return 0;
  else if (myMovie < currMovie) return -1;
  else return 1;
}

bool imdb::getCast(const film& movie, vector<string>& players) const {
  int *movieFileStart = (int*)movieFile;
  int movieNum = *movieFileStart; // Number of movies

  movieStruct movieSt;
  movieSt.movie = movie;
  movieSt.movieFile = movieFile;

  void* res = bsearch(&movieSt, movieFileStart + 1, movieNum, sizeof(int), compMov);
  if (res == NULL) return false;
  char* result = (char*)movieFileStart + *(int*)res;
  
  int infoSize = string(result).size() % 2 == 1 ? string(result).size() + 3 : string(result).size() + 2;
  short actorsNum = *(short*)(result + infoSize); // Number of actors in the movie
  infoSize += 2;
  if (infoSize % 4 != 0) infoSize += 2;

  // Fill the players vector with actors
  for (short i = 0; i < actorsNum; ++i) {
    players.push_back(string((char*)actorFile + *((int*)(result + infoSize) + i)));
  }
  
  return true;
}

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
